package com.terbuck.terbuck_be.domain.university.service;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.terbuck.terbuck_be.common.exception.BusinessException;
import com.terbuck.terbuck_be.common.exception.ErrorCode;
import com.terbuck.terbuck_be.domain.university.dto.CollegeResponse;
import com.terbuck.terbuck_be.domain.university.entity.College;
import com.terbuck.terbuck_be.domain.university.entity.University;
import com.terbuck.terbuck_be.domain.university.repository.CollegeRepository;
import com.terbuck.terbuck_be.domain.university.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final UniversityRepository universityRepository;

    public List<CollegeResponse> getCollegesByUniversityName(String universityName) {
        University university = universityRepository.findByName(universityName)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNIVERSITY_NOT_FOUND));

        return collegeRepository.findByUniversityId(university.getId()).stream()
                .map(CollegeResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createCollegesFromCsv(MultipartFile file) throws IOException, CsvException {
        // 1) CSV 파서/리더: 따옴표-감싼 필드 처리 + 1줄 헤더 스킵
        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')              // 기본 콤마
                .withIgnoreQuotations(false)     // 따옴표 유지하여 필드 통째로 읽기
                .build();

        try (Reader in = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVReader reader = new CSVReaderBuilder(in)
                     .withSkipLines(1)           // 헤더: university,college
                     .withCSVParser(parser)
                     .build()) {

            List<String[]> rows = reader.readAll();

            // 성능 최적화: 대학명 캐시 & 일괄 저장 리스트
            Map<String, University> uniCache = new HashMap<>();
            List<College> toSave = new ArrayList<>();

            for (String[] row : rows) {
                if (row.length < 2) continue; // 불완전한 행 방어
                String universityName = row[0] != null ? row[0].trim() : "";
                String collegesField  = row[1] != null ? row[1].trim() : "";

                if (universityName.isEmpty() || collegesField.isEmpty()) continue;

                // 대학 엔티티 조회(캐시)
                University university = uniCache.computeIfAbsent(universityName, name ->
                        universityRepository.findByName(name).orElse(null)
                );
                if (university == null) {
                    // 대학 미등록 시 SKIP (원하면 여기서 생성하도록 변경 가능)
                    continue;
                }

                // 2) “문과대학, 경영대학, …” → 개별 단과대학으로 분리
                String[] collegeNames = collegesField.split("\\s*,\\s*");
                for (String collegeName : collegeNames) {
                    if (collegeName.isBlank()) continue;

                    // 3) 중복 방지: 같은 대학 내 동일 명칭이 이미 있으면 건너뜀
                    boolean exists = collegeRepository.existsByNameAndUniversity(collegeName, university);
                    if (!exists) {
                        toSave.add(College.builder()
                                .name(collegeName)
                                .university(university)
                                .build());
                    }
                }
            }

            if (!toSave.isEmpty()) {
                collegeRepository.saveAll(toSave); // 배치 저장
            }
        }
    }
}
