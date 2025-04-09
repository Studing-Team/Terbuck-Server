package com.terbuck.terbuck_be.domain.member.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@Getter
@NoArgsConstructor(access = PROTECTED)
public abstract class Policy {

    private Boolean agreedToService;

    private Boolean agreedToEssentialInfo;

    private Boolean agreedToOptional;

    public Policy(Boolean agreedToService, Boolean agreedToEssentialInfo, Boolean agreedToOptional) {
        this.agreedToService = agreedToService;
        this.agreedToEssentialInfo = agreedToEssentialInfo;
        this.agreedToOptional = agreedToOptional;
    }
}
