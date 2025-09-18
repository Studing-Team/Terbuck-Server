package com.terbuck.terbuck_be.domain.member.dto;

import lombok.Builder;

@Builder
public record StudentIDPendingResponse(boolean isPending) {
    public static StudentIDPendingResponse of(boolean isPending) {
        return StudentIDPendingResponse.builder()
                .isPending(isPending)
                .build();
    }
}
