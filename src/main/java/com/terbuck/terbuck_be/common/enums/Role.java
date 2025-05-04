package com.terbuck.terbuck_be.common.enums;

public enum Role {

    ADMIN,
    MEMBER;

    public String toAuthority() {
        return "ROLE_" + this.name();
    }
}
