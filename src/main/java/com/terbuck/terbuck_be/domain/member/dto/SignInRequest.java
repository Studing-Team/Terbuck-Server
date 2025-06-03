package com.terbuck.terbuck_be.domain.member.dto;

import com.terbuck.terbuck_be.common.enums.University;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignInRequest {

    @NotNull(message = "university 값은 필수입니다.")
    private University university;

//   Member.additionalInfo()
//    this.policy = new Policy(true, true, true);
//    @NotNull(message = "서비스 약관 동의 값은 필수입니다.")
//    private Boolean agreedToService;
//
//    @NotNull(message = "필수 정보 제공 동의 값은 필수입니다.")
//    private Boolean agreedToEssentialInfo;
//
//    @NotNull(message = "선택 정보 제공 동의 값은 필수입니다.")
//    private Boolean agreedToOptional;
}
