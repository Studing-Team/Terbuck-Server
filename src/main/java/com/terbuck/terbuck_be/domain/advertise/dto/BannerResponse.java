package com.terbuck.terbuck_be.domain.advertise.dto;

import com.terbuck.terbuck_be.domain.advertise.entity.Banner;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BannerResponse {
    private Long id;
    private String title;
    private String imageURL;
    private String link;

    public static BannerResponse from(Banner banner) {
        return BannerResponse.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .imageURL(banner.getImageURL())
                .link(banner.getLink())
                .build();
    }
}