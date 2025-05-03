package com.terbuck.terbuck_be.domain.shop.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BenefitDto {

    private String title;
    private List<String> detailList = new ArrayList<>();

    public BenefitDto(String title) {
        this.title = title;
    }
}
