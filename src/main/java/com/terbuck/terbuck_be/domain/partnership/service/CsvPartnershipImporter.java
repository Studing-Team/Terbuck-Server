package com.terbuck.terbuck_be.domain.partnership.service;

import com.opencsv.CSVReader;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.partnership.entity.Institution;
import com.terbuck.terbuck_be.domain.partnership.entity.PartnerCategory;
import com.terbuck.terbuck_be.domain.partnership.entity.Partnership;
import com.terbuck.terbuck_be.domain.partnership.repository.JpaPartnershipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvPartnershipImporter {

    private final JpaPartnershipRepository partnershipRepository;

    private static final int COLUMN_LENGTH = 5;

    @Transactional
    public void importFromCsv(MultipartFile file, University file_univ) {

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] cols;
            boolean isFirst = true;
            int lineNumber = 0;

            while ((cols = csvReader.readNext()) != null) {
                lineNumber++;

                if (isFirst) {
                    isFirst = false;
                    continue; // 헤더 스킵
                }

                if (cols.length != COLUMN_LENGTH) {
                    log.warn("컬럼 수 부족 - Line {}: {}", lineNumber, Arrays.toString(cols));
                    continue;
                }

                String name = cols[0].trim();
                String institutionStr = cols[1].trim();
                String detail = cols[2].trim();
                String snsLink = cols[3].trim();
                String partnerCategoryStr = cols[4].trim();

                Institution institution = parseInstitution(institutionStr);
                PartnerCategory partnerCategory = parsePartnerCategory(partnerCategoryStr);

                Partnership partnership = Partnership.builder()
                        .name(name)
                        .university(file_univ)
                        .category(partnerCategory)
                        .institution(institution)
                        .detail(detail)
                        .snsLink(snsLink)
                        .build();

                partnershipRepository.save(partnership);
            }
            log.info("All shops parsed and saved successfully.");
        } catch (Exception e) {
            throw new RuntimeException("CSV 파싱 중 오류 발생" + e.getMessage(), e);
        }
    }

    private Institution parseInstitution(String value) {
        return Arrays.stream(Institution.values())
                .filter(cat -> cat.name().equalsIgnoreCase(value.trim()) || cat.toString().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 기관: " + value));
    }

    private PartnerCategory parsePartnerCategory(String value) {
        return Arrays.stream(PartnerCategory.values())
                .filter(cat -> cat.name().equalsIgnoreCase(value.trim()) || cat.toString().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 파트너 카테고리: " + value));
    }

}
