package com.terbuck.terbuck_be.domain.member.entity;

import com.terbuck.terbuck_be.common.entity.BaseTimeEntity;
import com.terbuck.terbuck_be.common.enums.Role;
import com.terbuck.terbuck_be.common.enums.SocialType;
import com.terbuck.terbuck_be.domain.university.entity.College;
import com.terbuck.terbuck_be.domain.university.entity.University;
import com.terbuck.terbuck_be.domain.member.dto.SignInRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String socialId;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id")
    private College college;

    @Embedded
    private Policy policy;

    private String refreshToken;

    @Column(name = "fcm_device_token")
    private String fcmDeviceToken;

    @Embedded
    private StudentID studentID;

    private Boolean isSignedUp;

    public Member(String socialId, SocialType socialType, University university, Policy policy, String refreshToken) {
        this.socialId = socialId;
        this.socialType = socialType;
        this.university = university;
        this.policy = policy;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String token) {
        this.refreshToken = token;
    }

    public void changeStudentID(StudentID studentID) {
        this.studentID = studentID;
    }

    public void additionalInfo(University university) {
        this.university = university;
        this.policy = new Policy(true, true, true);
        this.isSignedUp = true;
    }

    public void additionalInfoV2(University university, College college) {
        this.university = university;
        this.college = college;
        this.policy = new Policy(true, true, true);
        this.isSignedUp = true;
    }

    public void updateUniversity(University university) {
        this.university = university;
    }

    public void updateUniversityV2(University university, College college) {
        this.university = university;
        this.college = college;
    }

    public void updateStudentID(String studentIDImageURL, String studentNumber) {
        this.studentID = new StudentID(false, studentNumber, studentIDImageURL);
    }
    public void updateFcmDeviceToken(String token) {
        this.fcmDeviceToken = token;
    }
}
