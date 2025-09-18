package com.terbuck.terbuck_be.domain.info.dto;

import lombok.Builder;

@Builder
public record UpdateCheckResponse(
        boolean isUpdateNeeded,
        boolean isForceUpdate
) {
}
