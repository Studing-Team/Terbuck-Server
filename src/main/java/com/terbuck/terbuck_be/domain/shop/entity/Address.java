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

    public Address(String region, String city, String road, String buildingNumber, String etc) {
        this.region = region;
        this.city = city;
        this.road = road;
        this.buildingNumber = buildingNumber;
        this.etc = etc;
    }

    public static String StringOf(Address address) {
        StringBuilder sb = new StringBuilder();
        sb.append(address.getRegion())
                .append(" ")
                .append(address.getCity())
                .append(" ")
                .append(address.getRoad())
                .append(" ")
                .append(address.getBuildingNumber())
                .append(" ")
                .append(address.getEtc());

        return sb.toString();
    }
}
