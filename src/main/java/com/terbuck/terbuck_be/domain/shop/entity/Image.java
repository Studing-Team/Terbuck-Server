package com.terbuck.terbuck_be.domain.shop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    private String imageURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public Image(String imageURL) {
        this.imageURL = imageURL;
    }

    public void changeShop(Shop shop) {
        this.shop = shop;
        shop.getImageList().add(this);
    }
}
