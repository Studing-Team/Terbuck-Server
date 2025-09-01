package com.terbuck.terbuck_be.domain.university.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UniversityRequest {
    private String universityName;
    private String regionName;
    private boolean isRegistered;
}
