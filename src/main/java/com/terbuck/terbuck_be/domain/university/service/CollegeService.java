package com.terbuck.terbuck_be.domain.university.service;

import com.opencsv.CSVReader;
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
import java.nio.charset.StandardCharsets;
import java.util.List;
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
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            List<String[]> allRows = reader.readAll();
            for (String[] row : allRows) {
                String universityName = row[0];
                String collegeName = row[1];

                University university = universityRepository.findByName(universityName)
                        .orElse(null);

                if (university != null) {
                    College college = College.builder()
                            .name(collegeName)
                            .university(university)
                            .build();
                    collegeRepository.save(college);
                }
            }
        }
    }
}
