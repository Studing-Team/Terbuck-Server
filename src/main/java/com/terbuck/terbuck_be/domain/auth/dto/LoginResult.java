package com.terbuck.terbuck_be.domain.auth.dto;

public record LoginResult(
        String accessToken,
        String refreshToken,
        Long MemberId,
        boolean needsMoreInfo
) {}

