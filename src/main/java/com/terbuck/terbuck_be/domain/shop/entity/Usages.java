package com.terbuck.terbuck_be.domain.shop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Usages {

    @Id
    @GeneratedValue
    @Column(name = "usage_id")
    private Long id;

    private String introduction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public Usages(String introduction) {
        this.introduction = introduction;
    }

    public void changeShop(Shop shop) {
        this.shop = shop;
        shop.getUsagesList().add(this);
    }
}
