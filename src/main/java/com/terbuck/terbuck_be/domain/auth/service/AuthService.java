package com.terbuck.terbuck_be.domain.auth.service;

import com.terbuck.terbuck_be.domain.auth.dto.LoginResult;
import com.terbuck.terbuck_be.domain.auth.dto.ReIssueResponse;
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

    public ReIssueResponse reIssue(String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("잘못된 리프레시 토큰입니다.");
        }

        Long memberId = jwtTokenProvider.getUserIdFromToken(token);
        Member findMember = memberService.findMemberBy(memberId);

        if (!findMember.getRefreshToken().equals(token)) {
            throw new IllegalArgumentException("인증되지 않은 리프레시 토큰입니다.");
        }

        String accessToken = jwtTokenProvider.createAccessToken(memberId);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId);
        findMember.updateRefreshToken(refreshToken);

        return new ReIssueResponse(accessToken, refreshToken);
    }
}