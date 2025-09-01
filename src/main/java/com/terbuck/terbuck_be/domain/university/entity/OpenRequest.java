package com.terbuck.terbuck_be.domain.university.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OpenRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String universityName;

    private Long memberId;

    public OpenRequest(String universityName, Long memberId) {
        this.universityName = universityName;
        this.memberId = memberId;
    }
}
