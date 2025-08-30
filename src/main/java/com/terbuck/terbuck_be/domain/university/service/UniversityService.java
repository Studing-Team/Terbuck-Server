package com.terbuck.terbuck_be.domain.university.service;

import com.terbuck.terbuck_be.domain.university.dto.UniversityRequest;
import com.terbuck.terbuck_be.domain.university.dto.UniversityResponse;
import com.terbuck.terbuck_be.domain.university.entity.University;
import com.terbuck.terbuck_be.domain.university.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UniversityService {

    private final UniversityRepository universityRepository;

    public UniversityResponse createUniversity(UniversityRequest request) {
        University university = new University(request.getName());
        universityRepository.save(university);
        return UniversityResponse.from(university);
    }

    @Transactional(readOnly = true)
    public UniversityResponse getUniversity(Long id) {
        University university = universityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("University not found"));
        return UniversityResponse.from(university);
    }

    @Transactional(readOnly = true)
    public List<UniversityResponse> getAllUniversities() {
        return universityRepository.findAll().stream()
                .map(UniversityResponse::from)
                .collect(Collectors.toList());
    }

    public UniversityResponse updateUniversity(Long id, UniversityRequest request) {
        University university = universityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("University not found"));
        university.update(request.getName());
        return UniversityResponse.from(university);
    }

    public void deleteUniversity(Long id) {
        universityRepository.deleteById(id);
    }
}
