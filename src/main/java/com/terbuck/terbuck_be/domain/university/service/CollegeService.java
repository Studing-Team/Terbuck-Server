package com.terbuck.terbuck_be.domain.university.service;

import com.terbuck.terbuck_be.common.exception.BusinessException;
import com.terbuck.terbuck_be.common.exception.ErrorCode;
import com.terbuck.terbuck_be.domain.university.dto.CollegeResponse;
import com.terbuck.terbuck_be.domain.university.entity.University;
import com.terbuck.terbuck_be.domain.university.repository.CollegeRepository;
import com.terbuck.terbuck_be.domain.university.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
