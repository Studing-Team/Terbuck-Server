package com.terbuck.terbuck_be.domain.member.dto;

import com.terbuck.terbuck_be.common.enums.University;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PatchUnivRequest {

    private University university;
}
