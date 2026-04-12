package com.terbuck.terbuck_be.domain.partnership.entity;

import com.terbuck.terbuck_be.common.entity.BaseTimeEntity;
import com.terbuck.terbuck_be.domain.university.entity.University;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
public class Partnership extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    @Enumerated(EnumType.STRING)
    private PartnerCategory category;

    @Enumerated(EnumType.STRING)
    private Institution institution;

    @Column(columnDefinition = "TEXT")
    private String detail;

    private String snsLink;

    @OneToMany(mappedBy = "partnership", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PartnershipImage> imageList = new ArrayList<>();

}
