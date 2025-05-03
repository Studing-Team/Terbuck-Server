package com.terbuck.terbuck_be.domain.shop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Benefit {

    @Id
    @GeneratedValue
    @Column(name = "benefit_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "benefit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Detail> detailList = new ArrayList<>();

    public Benefit(String name) {
        this.name = name;
    }

    public void changeShop(Shop shop) {
        this.shop = shop;
        shop.getBenefitList().add(this);
    }
}
