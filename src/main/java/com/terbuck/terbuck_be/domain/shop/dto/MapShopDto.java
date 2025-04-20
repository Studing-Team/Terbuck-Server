package com.terbuck.terbuck_be.domain.shop.dto;

import com.terbuck.terbuck_be.domain.shop.entity.Address;
import com.terbuck.terbuck_be.domain.shop.entity.Benefit;
import com.terbuck.terbuck_be.domain.shop.entity.Location;
import com.terbuck.terbuck_be.domain.shop.entity.Shop;
import lombok.Data;

@Data
public class MapShopDto {

    private Long shopId;
    private String name;
    private String image;
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer benefitCount;

    public MapShopDto(Long shopId, String name, String image) {
        this.shopId = shopId;
        this.name = name;
        this.image = image;
    }

    public static MapShopDto of(Shop shop) {

        MapShopDto mapShopDto = new MapShopDto(
                shop.getId(),
                shop.getName(),
                shop.getImage()
        );

        String address = Address.StringOf(shop.getAddress());
        mapShopDto.setAddress(address);

        Location location = shop.getLocation();
        mapShopDto.setLatitude(location.getLatitude());
        mapShopDto.setLongitude(location.getLongitude());

        int count = 0;
        for (Benefit benefit : shop.getBenefitList()) {
            count += benefit.getDetailList().size();
        }
        mapShopDto.setBenefitCount(count);

        return mapShopDto;
    }
}
