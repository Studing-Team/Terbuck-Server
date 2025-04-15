package com.terbuck.terbuck_be.domain.shop.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public abstract class Location {

    private double latitude;

    private double longitude;
}
