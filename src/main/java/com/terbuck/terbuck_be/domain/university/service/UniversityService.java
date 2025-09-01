package com.terbuck.terbuck_be.domain.university.service;

import com.terbuck.terbuck_be.domain.university.dto.*;
import com.terbuck.terbuck_be.domain.university.entity.OpenRequest;
import com.terbuck.terbuck_be.domain.university.entity.University;
import com.terbuck.terbuck_be.domain.university.domain.Region;
import com.terbuck.terbuck_be.domain.university.repository.OpenRequestRepository;
import com.terbuck.terbuck_be.domain.university.repository.UniversityRepository;
import com.terbuck.terbuck_be.domain.university.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final RegionRepository regionRepository;
    private final OpenRequestRepository openRequestRepository;

    public UniversityResponse createUniversity(UniversityRequest request) {
        Region region = regionRepository.findByName(request.getRegionName())
                .orElseThrow(() -> new IllegalArgumentException("Region not found"));
        University university = new University(request.getUniversityName(), region, request.isRegistered());
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
        Region region = regionRepository.findByName(request.getRegionName())
                .orElseThrow(() -> new IllegalArgumentException("Region not found"));
        university.update(request.getUniversityName(), region, request.isRegistered());
        return UniversityResponse.from(university);
    }

    public void deleteUniversity(Long id) {
        universityRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<RegionUniversityResponse> getAllUniversitiesGroupedByRegion() {
        List<Region> regions = regionRepository.findAll();
        return regions.stream()
                .map(region -> {
                    List<UniversityResponse> universitiesInRegion = universityRepository.findByRegion(region).stream()
                            .map(UniversityResponse::from)
                            .collect(Collectors.toList());
                    return RegionUniversityResponse.builder()
                            .region(RegionResponse.from(region))
                            .universities(universitiesInRegion)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isUniversityRegistered(String universityName) {
        return universityRepository.findByName(universityName)
                .map(University::isRegistered)
                .orElse(false);
    }

    @Transactional
    public OpenRequestResponse requestUniversityOpen(String universityName, Long memberId) {
        // 1. Check if the university exists and is registered. If so, deny the request.
        universityRepository.findByName(universityName).ifPresent(university -> {
            if (university.isRegistered()) {
                throw new IllegalArgumentException("University is already registered.");
            }
        });

        // 2. Check if the user has already made an open request for this university with PENDING status.
        openRequestRepository.findByUniversityNameAndMemberId(universityName, memberId)
                .ifPresent(request -> {
                    throw new IllegalArgumentException("You have already requested to open this university.");
                });

        // 3. If not, create a new OpenRequest entry with PENDING status.
        OpenRequest openRequest = new OpenRequest(universityName, memberId);
        openRequestRepository.save(openRequest);

        return new OpenRequestResponse(openRequest.getId(), universityName);
    }

    @Transactional(readOnly = true)
    public Boolean checkAlreadyRequestOpen(Long memberId) {
        Optional<OpenRequest> openRequest = openRequestRepository.findByMemberId(memberId);
        return openRequest.isPresent();
    }
}
