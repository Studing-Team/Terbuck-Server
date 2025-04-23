package com.terbuck.terbuck_be.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessMessage {

//    UNIVERSITY_GET_SUCCESS(HttpStatus.OK.value(),"대학교이름 조회에 성공하였습니다."),

    HOME_SHOP_GET_SUCCESS(HttpStatus.OK.value(), "홈 화면 제휴업체 데이터 조회에 성공했습니다."),
    MAP_SHOP_GET_SUCCESS(HttpStatus.OK.value(), "터벅터벅 화면 제휴업체 데이터 조회에 성공했습니다."),
    ID_SHOP_GET_SUCCESS(HttpStatus.OK.value(), "ID를 통한 제휴 업체 단건 조회에 성공했습니다."),
    LOGIN_SUCCESS(HttpStatus.OK.value(), "로그인에 성공했습니다."),
    NEED_MORE_INFO(HttpStatus.OK.value(), "로그인에 성공했습니다. 그리고 추가 정보 입력이 필요합니다."),
    SIGN_IN_SUCCESS(HttpStatus.OK.value(), "추가 정보 입력이 완료되어 회원가입에 성공하였습니다."),
    UNIV_UPDATE_SUCCESS(HttpStatus.OK.value(), "대학교 변경에 성공했습니다."),
    STUDENTID_UPDATE_SUCCESS(HttpStatus.OK.value(), "학생증 등록 요청에 성공했습니다."),
    STUDENTID_GET_SUCCESS(HttpStatus.OK.value(), "학생증 조회에 성공했습니다."),
    ;
    private final int status;
    private final String message;
}
