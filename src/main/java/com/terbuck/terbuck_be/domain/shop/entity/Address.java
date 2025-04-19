package com.terbuck.terbuck_be.domain.shop.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String region;

    private String city;

    private String road;

    private String buildingNumber;

    private String etc;
}
