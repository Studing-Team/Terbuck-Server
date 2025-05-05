package com.terbuck.terbuck_be.domain.partnership.entity;

import com.terbuck.terbuck_be.common.enums.University;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Partnership {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private University university;

    @Enumerated(EnumType.STRING)
    private Institution institution;

    @Enumerated(EnumType.STRING)
    private PartnerCategory category;

    @OneToMany(mappedBy = "partnership")
    private List<PartnershipImage> imageList = new ArrayList<>();

}
