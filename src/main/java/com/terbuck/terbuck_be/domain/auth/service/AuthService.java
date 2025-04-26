package com.terbuck.terbuck_be.domain.auth.service;

import com.terbuck.terbuck_be.domain.auth.dto.LoginResult;
import com.terbuck.terbuck_be.domain.auth.dto.UserInfo;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.service.MemberService;
import com.terbuck.terbuck_be.global.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResult loginProcess(UserInfo userInfo) {
        Member member;
        if (memberService.isRegister(userInfo)) {
            member = memberService.findMemberBy(userInfo);
        } else {
            member = memberService.register(userInfo);
        }

        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
        member.updateRefreshToken(refreshToken);

        boolean needsAdditionalInfo = !member.getIsSignedUp();

        return new LoginResult(accessToken, refreshToken, member.getId(), needsAdditionalInfo);
    }
}