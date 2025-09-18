package com.terbuck.terbuck_be.domain.info.dto;

import com.terbuck.terbuck_be.domain.info.entity.AppVersion;
import lombok.Builder;

@Builder
public record AppVersionResponse(
        String os,
        String version,
        boolean isForceUpdate
) {
    public static AppVersionResponse from(AppVersion appVersion) {
        return AppVersionResponse.builder()
                .os(appVersion.getOs().name())
                .version(appVersion.getVersion())
                .isForceUpdate(appVersion.isForceUpdate())
                .build();
    }
}
