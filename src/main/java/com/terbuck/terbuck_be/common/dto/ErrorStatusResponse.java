package com.terbuck.terbuck_be.common.dto;

public record ErrorStatusResponse(
        int status,
        String message
) {
    public static ErrorStatusResponse of(int status, String message) {
        return new ErrorStatusResponse(status, message);
    }
}

