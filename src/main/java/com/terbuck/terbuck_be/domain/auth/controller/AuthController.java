package com.terbuck.terbuck_be.domain.auth.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.domain.auth.dto.LoginResponse;
import com.terbuck.terbuck_be.domain.auth.dto.ReIssueRequest;
import com.terbuck.terbuck_be.domain.auth.dto.ReIssueResponse;
import com.terbuck.terbuck_be.domain.auth.dto.UserInfo;
import com.terbuck.terbuck_be.domain.auth.service.AppleOAuthService;
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
    private final AppleOAuthService appleOAuthService;
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

    @GetMapping("/apple")
    public ResponseEntity<SuccessStatusResponse<LoginResponse>> appleLogin(@RequestParam String code, @RequestParam(required = false) String name) {
        UserInfo userInfo = appleOAuthService.getAppleUserInfo(code, name);

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

    @PostMapping("/apple/callback")
    public ResponseEntity<String> appleCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            @RequestParam(value = "user", required = false) String userJson
    ) {
        // userJson은 최초 로그인 시에만 옴
        if (userJson != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();

                JsonNode json = objectMapper.readTree(userJson);
                String firstName = json.path("name").path("firstName").asText();
                String lastName = json.path("name").path("lastName").asText();
                String userName = lastName + firstName;

                System.out.println("✅ Apple 유저 이름: " + userName);
            } catch (Exception e) {
                System.out.println("❌ user 파싱 실패: " + e.getMessage());
            }
        }

        System.out.println("✅ 받은 code: " + code);
        System.out.println("✅ 받은 state: " + state);

        return ResponseEntity.ok("code : " + code + "\nstate : " + state);
    }

    @PostMapping("/reissue")
    public ResponseEntity<SuccessStatusResponse<ReIssueResponse>> reissue(@RequestBody ReIssueRequest reIssueRequest) {
        ReIssueResponse reIssueResponse = authService.reIssue(reIssueRequest.getRefreshToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.AUTH_REISSUE_SUCCESS, reIssueResponse));
    }
}

