package com.terbuck.terbuck_be.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PatchUnivRequestV2 {

    @NotNull(message = "university 값은 필수입니다.")
    private String university;

    @NotNull(message = "college 값은 필수입니다.")
    private Long collegeId;
}
