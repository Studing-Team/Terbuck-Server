package com.terbuck.terbuck_be.domain.member.entity;

import com.terbuck.terbuck_be.common.enums.SocialType;
import com.terbuck.terbuck_be.common.enums.University;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    private Long socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private University university;

    @Embedded
    private Policy policy;

    private String refreshToken;

    @Embedded
    private StudentID studentID;

    private Boolean isSignedUp;

}
