package com.terbuck.terbuck_be.domain.shop.dto;

import com.terbuck.terbuck_be.common.entity.Image;
import com.terbuck.terbuck_be.domain.shop.entity.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShopResponse {

    private String name;
    private String shopLink;
    private List<String> imageList = new ArrayList<>();
    private String address;
    private Integer benefitCount;
    private List<BenefitDto> benefitList = new ArrayList<>();
    private List<UsageDto> usagesList = new ArrayList<>();

    public ShopResponse(String name, String shopLink) {
        this.name = name;
        this.shopLink = shopLink;
    }

    public static ShopResponse of(Shop shop) {
        ShopResponse shopResponse = new ShopResponse(
                shop.getName(),
                shop.getShopLink()
        );

        for (Image image : shop.getImageList()) {
            shopResponse.imageList.add(image.getImageURL());
        }

        String address = Address.StringOf(shop.getAddress());
        shopResponse.setAddress(address);

        List<BenefitDto> benefitList = shopResponse.getBenefitList();
        for (Benefit benefit : shop.getBenefitList()) {
            BenefitDto benefitDto = new BenefitDto(benefit.getName());
            List<String> detailList = benefitDto.getDetailList();
            for (Detail detail : benefit.getDetailList()) {
                detailList.add(detail.getDescription());
            }
            benefitList.add(benefitDto);
        }
        shopResponse.setBenefitCount(shop.getBenefitList().size());

        List<UsageDto> usagesList = shopResponse.getUsagesList();
        for (Usages usages : shop.getUsagesList()) {
            usagesList.add(new UsageDto(usages.getIntroduction()));
        }

        return shopResponse;
    }
}
