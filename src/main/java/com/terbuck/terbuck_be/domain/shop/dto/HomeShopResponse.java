package com.terbuck.terbuck_be.domain.shop.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HomeShopResponse {

    private List<HomeShopDto> list = new ArrayList<>();
}
