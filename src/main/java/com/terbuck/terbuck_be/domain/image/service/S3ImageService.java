package com.terbuck.terbuck_be.domain.image.service;

import com.terbuck.terbuck_be.domain.university.entity.University;
import com.terbuck.terbuck_be.domain.university.repository.UniversityRepository;
import com.terbuck.terbuck_be.domain.partnership.entity.Partnership;
import com.terbuck.terbuck_be.domain.partnership.entity.PartnershipImage;
import com.terbuck.terbuck_be.domain.partnership.repository.JpaPartnershipRepository;
import com.terbuck.terbuck_be.domain.shop.entity.Shop;
import com.terbuck.terbuck_be.domain.shop.entity.ShopImage;
import com.terbuck.terbuck_be.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import jakarta.persistence.EntityNotFoundException;
import java.text.Normalizer;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class S3ImageService {

    private static final Pattern THUMBNAIL_FILE_PATTERN = Pattern.compile(".*/1\\.[^/]+$");

    private final S3Client s3Client;
    private final ShopRepository shopRepository;
    private final JpaPartnershipRepository jpaPartnershipRepository;
    private final UniversityRepository universityRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private static final String STUDENTID_FOLDER_NAME = "studentID";

    private record ShopImageUpdateResult(String shopName, boolean success, boolean thumbnailUpdated, int imageCount, String reason) {
        static ShopImageUpdateResult success(String shopName, boolean thumbnailUpdated, int imageCount) {
            return new ShopImageUpdateResult(shopName, true, thumbnailUpdated, imageCount, null);
        }

        static ShopImageUpdateResult failure(String shopName, String reason) {
            return new ShopImageUpdateResult(shopName, false, false, 0, reason);
        }
    }

    public String uploadStudentIDImage(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString();
            String fullPath = STUDENTID_FOLDER_NAME + "/" + fileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fullPath)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            return getFileUrl(fullPath);
        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 중 오류가 발생했습니다." + e);
        }
    }

    private String getFileUrl(String fileName) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);
    }

    public void updateAllShopImagesByUniversity(Long universityId) {
        University university = universityRepository.findById(universityId).orElseThrow(() -> new IllegalArgumentException("University not found"));
        ListObjectsV2Response listResponse = null;
        String universityPrefix = null;

        for (String candidateName : normalizedCandidates(university.getName())) {
            String candidatePrefix = String.format("shop/%s/", candidateName);
            ListObjectsV2Response candidateResponse = listPrefixes(candidatePrefix);
            log.info("Tried shop image sync prefix. university={}, prefix={}, folderCount={}",
                    university.getName(), candidatePrefix, candidateResponse.commonPrefixes().size());

            if (!candidateResponse.commonPrefixes().isEmpty()) {
                universityPrefix = candidatePrefix;
                listResponse = candidateResponse;
                break;
            }

            if (listResponse == null) {
                listResponse = candidateResponse;
                universityPrefix = candidatePrefix;
            }
        }

        int folderCount = listResponse.commonPrefixes().size();
        int successCount = 0;
        int failureCount = 0;
        int thumbnailUpdatedCount = 0;
        int totalImageCount = 0;

        log.info("Starting shop image sync. university={}, prefix={}, folderCount={}", university.getName(), universityPrefix, folderCount);

        // CommonPrefixes: 폴더들 (업체 폴더 경로들)
        for (CommonPrefix commonPrefix : listResponse.commonPrefixes()) {
            String shopFolderKey = commonPrefix.prefix(); // 예: shop/광운대학교/국수천왕/

            // 폴더 이름만 추출: shop/광운대학교/국수천왕/ -> 국수천왕
            String[] parts = shopFolderKey.split("/");
            if (parts.length < 3) continue;
            String rawShopName = parts[2];
            String normalizedShopName = normalizeToNfc(rawShopName);

            ShopImageUpdateResult result = updateShopImages(university, universityPrefix, rawShopName, normalizedShopName);
            if (result.success()) {
                successCount++;
                totalImageCount += result.imageCount();
                if (result.thumbnailUpdated()) {
                    thumbnailUpdatedCount++;
                }
                log.info("Shop image sync success. university={}, rawShopName={}, normalizedShopName={}, imageCount={}, thumbnailUpdated={}",
                        university.getName(), rawShopName, result.shopName(), result.imageCount(), result.thumbnailUpdated());
                continue;
            }

            failureCount++;
            log.warn("Shop image sync failed. university={}, rawShopName={}, normalizedShopName={}, reason={}",
                    university.getName(), rawShopName, result.shopName(), result.reason());
        }

        log.info("Finished shop image sync. university={}, folderCount={}, successCount={}, failureCount={}, thumbnailUpdatedCount={}, totalImageCount={}",
                university.getName(), folderCount, successCount, failureCount, thumbnailUpdatedCount, totalImageCount);
    }

    private ShopImageUpdateResult updateShopImages(University university, String universityPrefix, String rawShopName, String normalizedShopName) {
        String prefix = String.format("%s%s/", universityPrefix, rawShopName);
        ListObjectsV2Response listResponse = listObjects(prefix);

        Shop shop;
        try {
            shop = shopRepository.findByUnivAndName(university, normalizedShopName);
        } catch (EntityNotFoundException e) {
            return ShopImageUpdateResult.failure(normalizedShopName, "SHOP_NOT_FOUND_IN_DB");
        }

        int imageCount = 0;
        boolean thumbnailUpdated = false;

        for (S3Object obj : listResponse.contents()) {
            if (obj.key().endsWith("/")) continue;

            String url = getFileUrl(obj.key());
            ShopImage image = new ShopImage(url);
            image.changeShop(shop);
            imageCount++;

            if (THUMBNAIL_FILE_PATTERN.matcher(obj.key()).matches()) {
                shop.changeThumbnailImage(url); // shop 객체에 thumbnail 필드 설정
                thumbnailUpdated = true;
            }
        }

        if (imageCount == 0) {
            return ShopImageUpdateResult.failure(normalizedShopName, "NO_IMAGE_FILES_IN_S3_FOLDER");
        }

        return ShopImageUpdateResult.success(normalizedShopName, thumbnailUpdated, imageCount);
    }

    private ListObjectsV2Response listPrefixes(String prefix) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(prefix)
                .delimiter("/")
                .build();
        return s3Client.listObjectsV2(listRequest);
    }

    private ListObjectsV2Response listObjects(String prefix) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(prefix)
                .build();
        return s3Client.listObjectsV2(listRequest);
    }

    private List<String> normalizedCandidates(String value) {
        Set<String> candidates = new LinkedHashSet<>();
        candidates.add(value);
        candidates.add(Normalizer.normalize(value, Normalizer.Form.NFC));
        candidates.add(Normalizer.normalize(value, Normalizer.Form.NFD));
        return List.copyOf(candidates);
    }

    private String normalizeToNfc(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFC);
    }

    public void updateAllPartnershipImagesByUniversity(Long universityId) {
        University university = universityRepository.findById(universityId).orElseThrow(() -> new IllegalArgumentException("University not found"));
        String partnershipUniversityPrefix = String.format("partnership/%s/", university.getName());

        // 대학교 폴더 하위의 모든 객체 가져오기
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(partnershipUniversityPrefix)
                .delimiter("/") // 폴더 단위로 끊기
                .build();

        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

        // CommonPrefixes: 폴더들 (업체 폴더 경로들)
        for (CommonPrefix commonPrefix : listResponse.commonPrefixes()) {
            String partnershipFolderKey = commonPrefix.prefix();

            String[] parts = partnershipFolderKey.split("/");
            if (parts.length < 3) continue;
            String partnershipName = parts[2];

            updatePartnershipImages(university, partnershipName);
        }
    }

    private void updatePartnershipImages(University university, String partnershipName) {
        String prefix = String.format("partnership/%s/%s/", university.getName(), partnershipName);

        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(prefix)
                .build();

        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

        Partnership partnership = jpaPartnershipRepository.findByUnivAndName(university, partnershipName);

        for (S3Object obj : listResponse.contents()) {
            if (obj.key().endsWith("/")) continue;

            String url = getFileUrl(obj.key());
            PartnershipImage image = new PartnershipImage(url);
            image.changePartnership(partnership);
        }
    }
}
