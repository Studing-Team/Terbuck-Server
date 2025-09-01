package com.terbuck.terbuck_be.domain.university.dto;

import com.terbuck.terbuck_be.domain.university.domain.Region;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegionResponse {
    private Long id;
    private String name;

    public static RegionResponse from(Region region) {
        return RegionResponse.builder()
                .id(region.getId())
                .name(region.getName())
                .build();
    }
}
