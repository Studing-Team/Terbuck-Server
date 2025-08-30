package com.terbuck.terbuck_be.domain.university.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Region(String name) {
        this.name = name;
    }

    public void update(String name) {
        this.name = name;
    }
}