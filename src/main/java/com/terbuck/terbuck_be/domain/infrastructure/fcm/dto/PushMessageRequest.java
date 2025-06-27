package com.terbuck.terbuck_be.domain.infrastructure.fcm.dto;

import lombok.Data;

@Data
public class PushMessageRequest {

    private String title;
    private String message;
}
