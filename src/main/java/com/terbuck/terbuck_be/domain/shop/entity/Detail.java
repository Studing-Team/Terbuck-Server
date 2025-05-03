package com.terbuck.terbuck_be.domain.shop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Detail {

    @Id
    @GeneratedValue
    private Long id;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benefit_id")
    private Benefit benefit;

    public void changeBenefit(Benefit benefit) {
        this.benefit = benefit;
        benefit.getDetailList().add(this);
    }

    public Detail(String description) {
        this.description = description;
    }
}
