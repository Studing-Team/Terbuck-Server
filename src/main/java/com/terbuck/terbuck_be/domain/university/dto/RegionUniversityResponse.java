package com.terbuck.terbuck_be.domain.university.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RegionUniversityResponse {
    private RegionResponse region;
    private List<UniversityResponse> universities;
}
