package com.terbuck.terbuck_be.domain.university.entity;

import com.terbuck.terbuck_be.domain.university.domain.Region;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region region;

    public University(String name, Region region) {
        this.name = name;
        this.region = region;
    }

    public void update(String name, Region region) {
        this.name = name;
        this.region = region;
    }
}
