package com.terbuck.terbuck_be.domain.shop.entity;

import com.terbuck.terbuck_be.common.enums.University;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Shop {

    @Id
    @GeneratedValue
    @Column(name = "shop_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private University university;

    @Enumerated(EnumType.STRING)
    private ShopCategory category;

    @Embedded
    private Address address;

    private String image;

    private String shopLink;

    @Embedded
    private Location location;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Benefit> benefitList = new ArrayList<>();

    public Shop(String name, University university, ShopCategory category, Address address, String image, String shopLink, Location location) {
        this.name = name;
        this.university = university;
        this.category = category;
        this.address = address;
        this.image = image;
        this.shopLink = shopLink;
        this.location = location;
    }
}
