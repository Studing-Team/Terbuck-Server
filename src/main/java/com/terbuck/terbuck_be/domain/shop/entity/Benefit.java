package com.terbuck.terbuck_be.domain.shop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Benefit {

    @Id
    @GeneratedValue
    @Column(name = "benefit_id")
    private Long id;

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public void changeShop(Shop shop) {
        this.shop = shop;
        shop.getBenefitList().add(this);
    }
}
