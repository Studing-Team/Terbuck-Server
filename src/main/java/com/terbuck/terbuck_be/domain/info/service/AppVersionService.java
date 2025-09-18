package com.terbuck.terbuck_be.domain.info.service;

import com.terbuck.terbuck_be.common.exception.BusinessException;
import com.terbuck.terbuck_be.common.exception.ErrorCode;
import com.terbuck.terbuck_be.domain.info.dto.AppVersionResponse;
import com.terbuck.terbuck_be.domain.info.dto.UpdateCheckResponse;
import com.terbuck.terbuck_be.domain.info.entity.AppVersion;
import com.terbuck.terbuck_be.domain.info.repository.AppVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppVersionService {

    private final AppVersionRepository appVersionRepository;

    public AppVersionResponse getAppVersion(AppVersion.OS os) {
        AppVersion appVersion = appVersionRepository.findByOs(os)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_APP_VERSION));
        return AppVersionResponse.from(appVersion);
    }

    public UpdateCheckResponse checkUpdate(AppVersion.OS os, String currentVersion) {
        AppVersion appVersion = appVersionRepository.findByOs(os)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_APP_VERSION));

        boolean isUpdateNeeded = isVersionLower(currentVersion, appVersion.getVersion());

        return UpdateCheckResponse.builder()
                .isUpdateNeeded(isUpdateNeeded)
                .isForceUpdate(isUpdateNeeded && appVersion.isForceUpdate())
                .build();
    }

    private boolean isVersionLower(String currentVersion, String minimumVersion) {
        String[] currentParts = currentVersion.split("\\.");
        String[] minimumParts = minimumVersion.split("\\.");

        int length = Math.max(currentParts.length, minimumParts.length);

        for (int i = 0; i < length; i++) {
            int currentPart = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            int minimumPart = i < minimumParts.length ? Integer.parseInt(minimumParts[i]) : 0;

            if (currentPart < minimumPart) {
                return true;
            }
            if (currentPart > minimumPart) {
                return false;
            }
        }
        return false;
    }
}
