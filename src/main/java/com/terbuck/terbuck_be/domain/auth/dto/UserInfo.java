package com.terbuck.terbuck_be.domain.auth.dto;

import com.terbuck.terbuck_be.common.enums.SocialType;

public record UserInfo(Long socialId, String name, SocialType socialType) {}

