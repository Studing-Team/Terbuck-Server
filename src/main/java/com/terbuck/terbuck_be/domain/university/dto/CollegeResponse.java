package com.terbuck.terbuck_be.domain.university.dto;

import com.terbuck.terbuck_be.domain.university.entity.College;
import lombok.Builder;

@Builder
public record CollegeResponse(
    Long id,
    String name
) {
    public static CollegeResponse from(College college) {
        return CollegeResponse.builder()
            .id(college.getId())
            .name(college.getName())
            .build();
    }
}
