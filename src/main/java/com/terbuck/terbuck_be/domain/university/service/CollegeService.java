package com.terbuck.terbuck_be.domain.university.service;

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

import java.util.List;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.FileReader;
import java.io.IOException;

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
    public void createCollegesFromCsv(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
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
