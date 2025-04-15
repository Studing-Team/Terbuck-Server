package com.terbuck.terbuck_be.domain.member.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StudentID {

    private Long studentNumber;

    private String idCardImage;

    public StudentID(Long studentNumber, String idCardImage) {
        this.studentNumber = studentNumber;
        this.idCardImage = idCardImage;
    }
}
