package com.terbuck.terbuck_be.domain.advertise.entity;

import com.terbuck.terbuck_be.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String imageURL;

    @Column(nullable = false)
    private String link;

    @Builder
    public Banner(String title, String imageURL, String link) {
        this.title = title;
        this.imageURL = imageURL;
        this.link = link;
    }

    public void update(String title, String imageURL, String link) {
        this.title = title;
        this.imageURL = imageURL;
        this.link = link;
    }
}
