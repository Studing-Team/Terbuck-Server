package com.terbuck.terbuck_be.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    AUTH_KAKAO_AUTHORIZATION_CODE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "카카오 인증 코드를 통해서 토큰을 발급받지 못했습니다."),
    AUTH_KAKAO_USERINFO_PARSING_FAIL(HttpStatus.BAD_REQUEST.value(), "카카오 유저 정보 파싱에 실패했습니다."),
    AUTH_REFRESH_TOKEN_INVALID(HttpStatus.BAD_REQUEST.value(), "인증되지 않은 리프레시 토큰입니다."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 사용자가 존재하지 않습니다."),
    MEMBER_STUDENTID_NOT_REGISTERED(HttpStatus.BAD_REQUEST.value(), "학생증이 등록되지 않았습니다."),

    SLACK_MESSAGE_FAILED(HttpStatus.BAD_REQUEST.value(), "슬랙 메시지 전송에 실패하였습니다."),

    SHOP_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 업체가 존재하지 않습니다.")
    ;

    private final int status;
    private final String message;
}
