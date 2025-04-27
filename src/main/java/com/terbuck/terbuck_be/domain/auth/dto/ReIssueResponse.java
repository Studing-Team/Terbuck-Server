package com.terbuck.terbuck_be.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReIssueResponse {

    private String accessToken;
    private String refreshToken;
}
