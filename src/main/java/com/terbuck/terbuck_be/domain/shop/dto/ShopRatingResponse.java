package com.terbuck.terbuck_be.domain.shop.dto;

import com.terbuck.terbuck_be.domain.shop.entity.Shop;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ShopRatingResponse {

    private Long shopId;
    private Double averageRating;
    private Long ratingCount;

    public static ShopRatingResponse of(Shop shop) {
        return new ShopRatingResponse(
                shop.getId(),
                shop.getAverageRating(),
                shop.getRatingCount()
        );
    }
}
