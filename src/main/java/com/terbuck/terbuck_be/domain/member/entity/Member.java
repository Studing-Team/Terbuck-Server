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
    @Column(name = "member_id")
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

    public Member(Long socialId, SocialType socialType, University university, Policy policy, String refreshToken) {
        this.socialId = socialId;
        this.socialType = socialType;
        this.university = university;
        this.policy = policy;
        this.refreshToken = refreshToken;
    }

    public void changeStudentID(StudentID studentID) {
        this.studentID = studentID;
    }
}
