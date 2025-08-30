package com.terbuck.terbuck_be.domain.university.controller;

import com.terbuck.terbuck_be.domain.university.dto.UniversityRequest;
import com.terbuck.terbuck_be.domain.university.dto.UniversityResponse;
import com.terbuck.terbuck_be.domain.university.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/university")
public class UniversityController {

    private final UniversityService universityService;

    @PostMapping
    public ResponseEntity<UniversityResponse> createUniversity(@RequestBody UniversityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(universityService.createUniversity(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversityResponse> getUniversity(@PathVariable Long id) {
        return ResponseEntity.ok(universityService.getUniversity(id));
    }

    @GetMapping
    public ResponseEntity<List<UniversityResponse>> getAllUniversities() {
        return ResponseEntity.ok(universityService.getAllUniversities());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UniversityResponse> updateUniversity(@PathVariable Long id, @RequestBody UniversityRequest request) {
        return ResponseEntity.ok(universityService.updateUniversity(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Long id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }
}
