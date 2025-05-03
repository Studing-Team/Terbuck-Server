package com.terbuck.terbuck_be.domain.auth.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private Boolean redirect;
    private String accessToken;
    private String refreshToken;

    public static LoginResponse of(LoginResult loginResult) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(loginResult.accessToken());
        loginResponse.setRefreshToken(loginResult.refreshToken());
        loginResponse.setRedirect(loginResult.needsMoreInfo());

        return loginResponse;
    }
}
