package com.terbuck.terbuck_be.domain.member.dto;

import com.terbuck.terbuck_be.common.enums.University;
import lombok.Data;

@Data
public class SignInRequest {

    private University university;

    private Boolean agreedToService;

    private Boolean agreedToEssentialInfo;

    private Boolean agreedToOptional;
}
