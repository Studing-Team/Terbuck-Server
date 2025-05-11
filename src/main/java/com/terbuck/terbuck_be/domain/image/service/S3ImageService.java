package com.terbuck.terbuck_be.domain.image.service;

import com.terbuck.terbuck_be.common.enums.University;
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

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class S3ImageService {

    private final S3Client s3Client;
    private final ShopRepository shopRepository;
    private final JpaPartnershipRepository jpaPartnershipRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private static final String STUDENTID_FOLDER_NAME = "studentID";

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

    public void updateAllShopImagesByUniversity(University university) {
        String universityPrefix = String.format("shop/%s/", university);

        // 대학교 폴더 하위의 모든 객체 가져오기
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(universityPrefix)
                .delimiter("/") // 폴더 단위로 끊기
                .build();

        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

        // CommonPrefixes: 폴더들 (업체 폴더 경로들)
        for (CommonPrefix commonPrefix : listResponse.commonPrefixes()) {
            String shopFolderKey = commonPrefix.prefix(); // 예: shop/광운대학교/국수천왕/

            // 폴더 이름만 추출: shop/광운대학교/국수천왕/ -> 국수천왕
            String[] parts = shopFolderKey.split("/");
            if (parts.length < 3) continue;
            String shopName = parts[2];

            updateShopImages(university, shopName);
        }
    }

    private void updateShopImages(University university, String shopName) {
        String prefix = String.format("shop/%s/%s/", university, shopName);

        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(prefix)
                .build();

        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

        Shop shop = shopRepository.findByUnivAndName(university, shopName);

        for (S3Object obj : listResponse.contents()) {
            if (obj.key().endsWith("/")) continue;

            String url = getFileUrl(obj.key());
            ShopImage image = new ShopImage(url);
            image.changeShop(shop);

            if (obj.key().endsWith("1.png")) {
                shop.changeThumbnailImage(url); // shop 객체에 thumbnail 필드 설정
            }
        }
    }

    public void updateAllPartnershipImagesByUniversity(University university) {
        String partnershipUniversityPrefix = String.format("partnership/%s/", university);

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
        String prefix = String.format("partnership/%s/%s/", university, partnershipName);

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
