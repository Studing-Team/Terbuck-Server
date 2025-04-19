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

    @Embedded
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

}
