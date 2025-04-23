package com.terbuck.terbuck_be.domain.member.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudentID {

    private Boolean isRegistered;

    private String studentNumber;

    private String idCardImage;

    public StudentID(Boolean isRegistered, String studentNumber, String idCardImage) {
        this.isRegistered = isRegistered;
        this.studentNumber = studentNumber;
        this.idCardImage = idCardImage;
    }
}
