package com.terbuck.terbuck_be.domain.university.controller;

import com.terbuck.terbuck_be.domain.university.dto.RegionRequest;
import com.terbuck.terbuck_be.domain.university.dto.RegionResponse;
import com.terbuck.terbuck_be.domain.university.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/regions")
public class RegionController {

    private final RegionService regionService;

    @PostMapping
    public ResponseEntity<RegionResponse> createRegion(@RequestBody RegionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(regionService.createRegion(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionResponse> getRegion(@PathVariable Long id) {
        return ResponseEntity.ok(regionService.getRegion(id));
    }

    @GetMapping
    public ResponseEntity<List<RegionResponse>> getAllRegions() {
        return ResponseEntity.ok(regionService.getAllRegions());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionResponse> updateRegion(@PathVariable Long id, @RequestBody RegionRequest request) {
        return ResponseEntity.ok(regionService.updateRegion(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
}
