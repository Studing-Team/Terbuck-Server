package com.terbuck.terbuck_be.domain.auth.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.domain.auth.dto.LoginResponse;
import com.terbuck.terbuck_be.domain.auth.dto.ReIssueRequest;
import com.terbuck.terbuck_be.domain.auth.dto.ReIssueResponse;
import com.terbuck.terbuck_be.domain.auth.dto.UserInfo;
import com.terbuck.terbuck_be.domain.auth.service.AuthService;
import com.terbuck.terbuck_be.domain.auth.service.KakaoOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final KakaoOAuthService kakaoOAuthService;
    private final AuthService authService; // 내부 회원 가입/로그인 처리

    @GetMapping("/kakao")
    public ResponseEntity<SuccessStatusResponse<LoginResponse>> kakaoLogin(@RequestParam String code) {
        String kakaoAccessToken = kakaoOAuthService.getAccessToken(code);
        UserInfo userInfo = kakaoOAuthService.getKakaoUserInfo(kakaoAccessToken);

        // 해당 회원 로그인 처리( 토큰 발급 ) + 신규 가입 회원인지 확인
        LoginResponse loginResponse = LoginResponse.of(authService.loginProcess(userInfo));

        SuccessMessage successMessage;
        if (loginResponse.getRedirect()) {
            successMessage = SuccessMessage.NEED_MORE_INFO;
        } else {
            successMessage = SuccessMessage.LOGIN_SUCCESS;
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(successMessage, loginResponse));
    }

    @GetMapping("/kakao/callback")
    public String kakaoCallback(@RequestParam String code) {

        return "code : " + code;
    }

//    @GetMapping("/apple")
//    public ResponseEntity<SuccessStatusResponse<LoginResponse>> appleLogin(@RequestParam String code) {
//        String kakaoAccessToken = kakaoOAuthService.getAccessToken(code);
//        UserInfo userInfo = kakaoOAuthService.getKakaoUserInfo(kakaoAccessToken);
//
//        // 해당 회원 로그인 처리( 토큰 발급 ) + 신규 가입 회원인지 확인
//        LoginResponse loginResponse = LoginResponse.of(authService.loginProcess(userInfo));
//
//        SuccessMessage successMessage;
//        if (loginResponse.getRedirect()) {
//            successMessage = SuccessMessage.NEED_MORE_INFO;
//        }else{
//            successMessage = SuccessMessage.LOGIN_SUCCESS;
//        }
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(SuccessStatusResponse.of(successMessage, loginResponse));
//    }

    @GetMapping("/apple/callback")
    public String appleCallback(@RequestParam String code) {

        return "code : " + code;
    }

    @PostMapping("/reissue")
    public ResponseEntity<SuccessStatusResponse<ReIssueResponse>> reissue(@RequestBody ReIssueRequest reIssueRequest) {
        ReIssueResponse reIssueResponse = authService.reIssue(reIssueRequest.getRefreshToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.AUTH_REISSUE_SUCCESS, reIssueResponse));
    }
}

