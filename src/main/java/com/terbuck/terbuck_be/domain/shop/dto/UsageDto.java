package com.terbuck.terbuck_be.domain.shop.dto;

import lombok.Data;

@Data
public class UsageDto {

    private String introduction;

    public UsageDto(String introduction) {
        this.introduction = introduction;
    }
}
