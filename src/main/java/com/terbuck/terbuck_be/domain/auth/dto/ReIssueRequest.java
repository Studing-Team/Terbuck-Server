package com.terbuck.terbuck_be.domain.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReIssueRequest {

    private String refreshToken;
}
