package com.terbuck.terbuck_be.domain.university.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OpenRequestResponse {
    private Long openRequestId;
    private String universityName;
}
