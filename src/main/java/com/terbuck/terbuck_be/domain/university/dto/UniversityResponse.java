package com.terbuck.terbuck_be.domain.university.dto;

import com.terbuck.terbuck_be.domain.university.entity.University;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UniversityResponse {
    private Long id;
    private String name;

    public static UniversityResponse from(University university) {
        return UniversityResponse.builder()
                .id(university.getId())
                .name(university.getName())
                .build();
    }
}
