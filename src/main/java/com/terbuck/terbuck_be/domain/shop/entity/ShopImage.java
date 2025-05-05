package com.terbuck.terbuck_be.domain.shop.entity;

import com.terbuck.terbuck_be.common.entity.Image;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopImage extends Image {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public ShopImage(String imageURL) {
        super(imageURL);
    }

    public void changeShop(Shop shop) {
        this.shop = shop;
        shop.getImageList().add(this);
    }
}
