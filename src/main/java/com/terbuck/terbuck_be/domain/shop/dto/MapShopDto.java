package com.terbuck.terbuck_be.domain.shop.dto;

import com.terbuck.terbuck_be.domain.shop.entity.*;
import lombok.Data;

@Data
public class MapShopDto {

    private Long shopId;
    private ShopCategory category;
    private String name;
    private String thumbnailImage;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer benefitCount;

    public MapShopDto(Long shopId, ShopCategory category, String name, String thumbnailImage) {
        this.shopId = shopId;
        this.category = category;
        this.name = name;
        this.thumbnailImage = thumbnailImage;
    }

    public static MapShopDto of(Shop shop) {

        MapShopDto mapShopDto = new MapShopDto(
                shop.getId(),
                shop.getCategory(),
                shop.getName(),
                shop.getThumbnailImage()
        );

        String address = Address.StringOf(shop.getAddress());
        mapShopDto.setAddress(address);

        Location location = shop.getLocation();
        mapShopDto.setLatitude(location.getLatitude());
        mapShopDto.setLongitude(location.getLongitude());

        mapShopDto.setBenefitCount(shop.getBenefitList().size());

        return mapShopDto;
    }
}
