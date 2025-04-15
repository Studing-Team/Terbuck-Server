package com.terbuck.terbuck_be.domain.shop.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public abstract class Address {

    private String region;

    private String city;

    private String road;

    private String buildingNumber;

    private String detail;

    private String etc;
}
