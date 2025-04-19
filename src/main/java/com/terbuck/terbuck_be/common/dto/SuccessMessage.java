package com.terbuck.terbuck_be.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessMessage {

//    UNIVERSITY_GET_SUCCESS(HttpStatus.OK.value(),"대학교이름 조회에 성공하였습니다."),

    HOME_SHOP_GET_SUCCESS(HttpStatus.OK.value(), "홈 화면 제휴업체 데이터 조회에 성공했습니다.")
    ;
    private final int status;
    private final String message;
}
