package com.terbuck.terbuck_be.domain.university.controller;

import com.opencsv.exceptions.CsvException;
import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.domain.university.dto.*;
import com.terbuck.terbuck_be.domain.university.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import com.terbuck.terbuck_be.domain.university.dto.CollegeResponse;
import com.terbuck.terbuck_be.domain.university.service.CollegeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/university")
public class UniversityController {

    private final UniversityService universityService;
    private final CollegeService collegeService;

    @PostMapping
    public ResponseEntity<SuccessStatusResponse<UniversityResponse>> createUniversity(@RequestBody UniversityRequest request) {
        UniversityResponse universityResponse = universityService.createUniversity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessStatusResponse.of(SuccessMessage.UNIVERSITY_CREATE_SUCCESS, universityResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessStatusResponse<UniversityResponse>> getUniversity(@PathVariable Long id) {
        UniversityResponse universityResponse = universityService.getUniversity(id);
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.UNIVERSITY_GET_SUCCESS, universityResponse));
    }

    @GetMapping
    public ResponseEntity<SuccessStatusResponse<List<UniversityResponse>>> getAllUniversities() {
        List<UniversityResponse> universityResponses = universityService.getAllUniversities();
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.UNIVERSITIES_GET_SUCCESS, universityResponses));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessStatusResponse<UniversityResponse>> updateUniversity(@PathVariable Long id, @RequestBody UniversityRequest request) {
        UniversityResponse universityResponse = universityService.updateUniversity(id, request);
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.UNIVERSITY_UPDATE_SUCCESS, universityResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessStatusResponse<Void>> deleteUniversity(@PathVariable Long id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.UNIVERSITY_DELETE_SUCCESS));
    }

    @GetMapping("/by-region")
    public ResponseEntity<SuccessStatusResponse<List<RegionUniversityResponse>>> getAllUniversitiesGroupedByRegion() {
        List<RegionUniversityResponse> regionUniversityResponses = universityService.getAllUniversitiesGroupedByRegion();
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.UNIVERSITIES_BY_REGION_GET_SUCCESS, regionUniversityResponses));
    }

    @GetMapping("/is-registered")
    public ResponseEntity<SuccessStatusResponse<Boolean>> isUniversityRegistered(@RequestParam String universityName) {
        boolean isRegistered = universityService.isUniversityRegistered(universityName);
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.UNIVERSITY_REGISTRATION_CHECK_SUCCESS, isRegistered));
    }

    @PostMapping("/open")
    public ResponseEntity<SuccessStatusResponse<OpenRequestResponse>> createOpenUniversityRequest(
            @AuthenticationPrincipal Long memberId,
            @RequestParam String universityName) {
        OpenRequestResponse openRequestResponse = universityService.requestUniversityOpen(universityName, memberId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.OPEN_REQUEST_SUCCESS, openRequestResponse));
    }

    @GetMapping("/open")
    public ResponseEntity<SuccessStatusResponse<Boolean>> getOpenUniversityRequest(
            @AuthenticationPrincipal Long memberId) {
        boolean hasRequested = universityService.checkAlreadyRequestOpen(memberId);
        return ResponseEntity.ok(SuccessStatusResponse.of(SuccessMessage.OPEN_REQUEST_CHECK_SUCCESS, hasRequested));
    }

    @GetMapping("/colleges")
    public SuccessStatusResponse<List<CollegeResponse>> getCollegesByUniversityName(@RequestParam("name") String name) {
        return SuccessStatusResponse.of(SuccessMessage.SUCCESS_GET_COLLEGES, collegeService.getCollegesByUniversityName(name));
    }

    @PostMapping("/colleges/csv")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessStatusResponse<Void>> createCollegesFromCsv() throws IOException, CsvException {
        collegeService.createCollegesFromCsv("univ_college.csv");
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessStatusResponse.of(SuccessMessage.SUCCESS_CREATE_COLLEGES));
    }
}
