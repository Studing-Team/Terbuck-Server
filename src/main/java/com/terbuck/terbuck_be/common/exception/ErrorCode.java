package com.terbuck.terbuck_be.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    KAKAO_AUTHORIZATION_CODE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "카카오 인증 코드를 통해서 토큰을 발급받지 못했습니다."),
    KAKAO_USERINFO_PARSING_FAIL(HttpStatus.BAD_REQUEST.value(), "카카오 유저 정보 파싱에 실패했습니다.")
    ;

    private final int status;
    private final String message;
}
