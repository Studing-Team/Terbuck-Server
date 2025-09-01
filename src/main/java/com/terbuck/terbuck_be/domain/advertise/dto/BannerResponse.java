package com.terbuck.terbuck_be.domain.advertise.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BannerResponse {
    private String bannerImageUrl;

    public static BannerResponse of(String bannerImageUrl) {
        return BannerResponse.builder()
                .bannerImageUrl(bannerImageUrl)
                .build();
    }
}
