package com.terbuck.terbuck_be.domain.partnership.entity;

import com.terbuck.terbuck_be.common.enums.University;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
public class Partnership {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private University university;

    @Enumerated(EnumType.STRING)
    private PartnerCategory category;

    @Enumerated(EnumType.STRING)
    private Institution institution;

    @Lob
    private String detail;

    private String snsLink;

    @OneToMany(mappedBy = "partnership", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartnershipImage> imageList = new ArrayList<>();

}
