package com.terbuck.terbuck_be.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Image {

    @Id
    @GeneratedValue
    private Long id;

    private String imageURL;

    public Image(String imageURL) {
        this.imageURL = imageURL;
    }
}
