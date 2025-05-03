package com.terbuck.terbuck_be.domain.image.dto;

import com.terbuck.terbuck_be.common.enums.University;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateShopRequest {

    private University university;
}
