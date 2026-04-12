package com.terbuck.terbuck_be.domain.shop.entity;

import com.terbuck.terbuck_be.common.entity.BaseTimeEntity;
import com.terbuck.terbuck_be.domain.university.entity.University;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Column(nullable = false)
    private Long totalRatingScore = 0L;

    @Column(nullable = false)
    private Long ratingCount = 0L;

    @Column(nullable = false)
    private Long viewCount = 0L;

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
        this.totalRatingScore = 0L;
        this.ratingCount = 0L;
        this.viewCount = 0L;
    }

    public void changeThumbnailImage(String url) {
        this.thumbnailImage = url;
    }

    public void addRating(int score) {
        if (this.totalRatingScore == null) {
            this.totalRatingScore = 0L;
        }
        if (this.ratingCount == null) {
            this.ratingCount = 0L;
        }
        this.totalRatingScore += score;
        this.ratingCount++;
    }

    public Double getAverageRating() {
        if (ratingCount == null || ratingCount == 0) {
            return 0.0;
        }

        return BigDecimal.valueOf(totalRatingScore)
                .divide(BigDecimal.valueOf(ratingCount), 1, RoundingMode.HALF_UP)
                .doubleValue();
    }

    public void increaseViewCount() {
        if (this.viewCount == null) {
            this.viewCount = 0L;
        }
        this.viewCount++;
    }
}
