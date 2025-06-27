package com.terbuck.terbuck_be.domain.infrastructure.fcm.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.domain.infrastructure.fcm.dto.DeviceTokenRequest;
import com.terbuck.terbuck_be.domain.infrastructure.fcm.dto.PushMessageRequest;
import com.terbuck.terbuck_be.domain.infrastructure.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fcm")
public class FCMController {

    private final FcmService fcmService;

    @PostMapping("/token")
    public ResponseEntity<SuccessStatusResponse<?>> register(@RequestBody DeviceTokenRequest deviceToken,
                                                          @AuthenticationPrincipal Long userID) {
        fcmService.saveFcmToken(userID, deviceToken.getDeviceToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.FCM_DEVICE_TOKEN_REGISTER_SUCCESS));
    }

    @PostMapping("/message")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessStatusResponse<Void>> sendPushMessage(@RequestBody PushMessageRequest pushMessageRequest,
                                                                    @AuthenticationPrincipal Long userID) {
        fcmService.ManualSendPush(userID, pushMessageRequest.getTitle(), pushMessageRequest.getMessage());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.FCM_MANUAL_PUSH_MESSAGE_SUCCESS));
    }
}
