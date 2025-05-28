package com.terbuck.terbuck_be.domain.shop.dto;

import com.terbuck.terbuck_be.domain.shop.entity.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HomeShopDto {

    private Long shopId;
    private ShopCategory category;
    private String name;
    private String address;
    private List<BenefitDto> benefitList = new ArrayList<>();

    public HomeShopDto(Long shopId, ShopCategory category, String name) {
        this.shopId = shopId;
        this.category = category;
        this.name = name;
    }

    public static HomeShopDto of(Shop shop) {

        HomeShopDto homeShopDto = new HomeShopDto(
                shop.getId(),
                shop.getCategory(),
                shop.getName()
        );

        String address = Address.StringOf(shop.getAddress());
        homeShopDto.setAddress(address);

        List<BenefitDto> benefitList = homeShopDto.getBenefitList();
        for (Benefit benefit : shop.getBenefitList()) {
            BenefitDto benefitDto = new BenefitDto(benefit.getName());
            List<String> detailList = benefitDto.getDetailList();
            for (Detail detail : benefit.getDetailList()) {
                detailList.add(detail.getDescription());
            }
            benefitList.add(benefitDto);
        }

        return homeShopDto;
    }
}
