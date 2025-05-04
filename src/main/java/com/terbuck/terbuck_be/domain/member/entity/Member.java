package com.terbuck.terbuck_be.domain.member.entity;

import com.terbuck.terbuck_be.common.enums.Role;
import com.terbuck.terbuck_be.common.enums.SocialType;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.member.dto.SignInRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private Long socialId;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

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

    public Member(Long socialId, SocialType socialType, University university, Policy policy, String refreshToken) {
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

    public void additionalInfo(SignInRequest request) {
        this.university = request.getUniversity();
        this.policy = new Policy(request.getAgreedToService(), request.getAgreedToEssentialInfo(), request.getAgreedToOptional());
        this.isSignedUp = true;
    }

    public void updateUniversity(University university) {
        this.university = university;
    }

    public void updateStudentID(String studentIDImageURL, String studentNumber) {
        this.studentID = new StudentID(false, studentNumber, studentIDImageURL);
    }
}
