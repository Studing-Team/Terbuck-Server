package com.terbuck.terbuck_be.domain.university.service;

import com.terbuck.terbuck_be.domain.university.domain.Region;
import com.terbuck.terbuck_be.domain.university.dto.RegionRequest;
import com.terbuck.terbuck_be.domain.university.dto.RegionResponse;
import com.terbuck.terbuck_be.domain.university.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RegionService {

    private final RegionRepository regionRepository;

    public RegionResponse createRegion(RegionRequest request) {
        Region region = new Region(request.getName());
        regionRepository.save(region);
        return RegionResponse.from(region);
    }

    @Transactional(readOnly = true)
    public RegionResponse getRegion(Long id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Region not found"));
        return RegionResponse.from(region);
    }

    @Transactional(readOnly = true)
    public List<RegionResponse> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(RegionResponse::from)
                .collect(Collectors.toList());
    }

    public RegionResponse updateRegion(Long id, RegionRequest request) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Region not found"));
        region.update(request.getName());
        regionRepository.save(region);
        return RegionResponse.from(region);
    }

    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }
}
