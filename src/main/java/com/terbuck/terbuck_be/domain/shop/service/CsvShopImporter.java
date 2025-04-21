package com.terbuck.terbuck_be.domain.shop.service;

import com.opencsv.CSVReader;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.entity.*;
import com.terbuck.terbuck_be.domain.shop.repository.JpaShopRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvShopImporter {

    private final JpaShopRepository shopRepository;
    private final EntityManager em;

    @Transactional
    public void importFromCsv(MultipartFile file, University file_univ) {
        log.info("importFromCsv");

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String[] cols;
            boolean isFirst = true;
            int lineNumber = 0;

            while ((cols = csvReader.readNext()) != null) {
                lineNumber++;
                log.info("Line {}: {}", lineNumber, Arrays.toString(cols));

                if (isFirst) {
                    isFirst = false;
                    continue; // 헤더 스킵
                }

                if (cols.length < 8) {
                    log.warn("컬럼 수 부족 - Line {}: {}", lineNumber, Arrays.toString(cols));
                    continue;
                }

                String name = cols[0].trim();
                String categoryStr = cols[1].trim();
                String benefitRaw = cols[2].trim();
                String fullAddress = cols[4].trim();
                double lat = Double.parseDouble(cols[5].trim());
                double lng = Double.parseDouble(cols[6].trim());
                String shopLink = cols[7].trim();

                ShopCategory category = parseShopCategory(categoryStr);
                University university = file_univ;

                Address address = parseAddress(fullAddress);
                Location location = new Location(lat, lng);

                Shop shop = new Shop(name, university, category, address, "", shopLink, location);

                List<Benefit> benefitList = parseBenefits(benefitRaw, shop);
                shop.getBenefitList().addAll(benefitList);

                shopRepository.save(shop);
            }
            em.flush();

            log.info("All shops parsed and saved successfully.");
        } catch (Exception e) {
            throw new RuntimeException("CSV 파싱 중 오류 발생", e);
        }
    }

    private List<Benefit> parseBenefits(String benefitRaw, Shop shop) {
        List<Benefit> benefits = new ArrayList<>();
        if (benefitRaw == null || benefitRaw.isBlank()) return benefits;

        String[] lines = benefitRaw.split("\n");
        Pattern pattern = Pattern.compile("^(\\d+)\\.\\s*(.+)$");

        Benefit currentBenefit = null;

        for (String line : lines) {
            Matcher matcher = pattern.matcher(line.trim());

            if (matcher.matches()) {
                currentBenefit = new Benefit(matcher.group(2).trim());
                currentBenefit.changeShop(shop);
                benefits.add(currentBenefit);
            } else if (currentBenefit != null && !line.trim().isEmpty()) {
                Detail detail = new Detail(line.trim());
                detail.changeBenefit(currentBenefit);
            }
        }

        return benefits;
    }

    private ShopCategory parseShopCategory(String value) {
        return Arrays.stream(ShopCategory.values())
                .filter(cat -> cat.name().equalsIgnoreCase(value.trim()) || cat.toString().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 카테고리: " + value));
    }

    private Address parseAddress(String fullAddress) {
        String[] parts = fullAddress.trim().split("\\s+");

        String region = parts.length > 0 ? parts[0] : "";
        String city = parts.length > 1 ? parts[1] : "";
        String road = parts.length > 2 ? parts[2] : "";
        String buildingNumber = parts.length > 3 ? parts[3] : "";

        String etc = parts.length > 4 ? String.join(" ", Arrays.copyOfRange(parts, 4, parts.length)) : null;

        return new Address(region, city, road, buildingNumber, etc);
    }

}
