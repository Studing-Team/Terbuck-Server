package com.terbuck.terbuck_be.domain.shop.entity;

import com.terbuck.terbuck_be.common.entity.BaseTimeEntity;
import com.terbuck.terbuck_be.domain.university.entity.University;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Shop extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    @Enumerated(EnumType.STRING)
    private ShopCategory category;

    @Embedded
    private Address address;

    private String thumbnailImage;

    private String shopLink;

    @Embedded
    private Location location;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Benefit> benefitList = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Usages> usagesList = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShopImage> imageList = new ArrayList<>();

    public Shop(String name, University university, ShopCategory category, Address address, String thumbnailImage, String shopLink, Location location) {

        this.name = name;
        this.university = university;
        this.category = category;
        this.address = address;
        this.thumbnailImage = thumbnailImage;
        this.shopLink = shopLink;
        this.location = location;
    }

    public void changeThumbnailImage(String url) {
        this.thumbnailImage = url;
    }
}
