package com.terbuck.terbuck_be.domain.auth.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.domain.auth.dto.LoginResponse;
import com.terbuck.terbuck_be.domain.auth.dto.UserInfo;
import com.terbuck.terbuck_be.domain.auth.service.AuthService;
import com.terbuck.terbuck_be.domain.auth.service.KakaoOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final AuthService authService; // 내부 회원 가입/로그인 처리

    @GetMapping("/kakao/callback")
    public ResponseEntity<SuccessStatusResponse<LoginResponse>> kakaoCallback(@RequestParam String code) {
        String kakaoAccessToken = kakaoOAuthService.getAccessToken(code);
        UserInfo userInfo = kakaoOAuthService.getKakaoUserInfo(kakaoAccessToken);

        // 해당 회원 로그인 처리( 토큰 발급 ) + 신규 가입 회원인지 확인
        LoginResponse loginResponse = LoginResponse.of(authService.loginProcess(userInfo));

        SuccessMessage successMessage = SuccessMessage.LOGIN_SUCCESS;
        if (loginResponse.getRedirect()) {
            successMessage = SuccessMessage.NEED_MORE_INFO;
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(successMessage, loginResponse));
    }
}

