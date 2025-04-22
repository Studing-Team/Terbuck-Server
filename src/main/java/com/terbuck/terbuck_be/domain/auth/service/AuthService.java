package com.terbuck.terbuck_be.domain.auth.service;

import com.terbuck.terbuck_be.domain.auth.dto.LoginResult;
import com.terbuck.terbuck_be.domain.auth.dto.UserInfo;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.repository.JpaMemberRepository;
import com.terbuck.terbuck_be.global.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JpaMemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResult loginOrSignup(UserInfo userInfo) {
        // 1. 기존 회원 여부 확인
        Member member = memberRepository.findBySocialIdAndSocialType(userInfo.socialId(), userInfo.socialType())
                .orElseGet(() -> {
                    // 2. 없으면 회원가입
                    Member newMember = createMember(userInfo);
                    Long savedMemberId = memberRepository.signUp(newMember);

                    return memberRepository.findById(savedMemberId);
                });

        // 3. JWT 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

        // 4. 추가 정보 입력 여부
        boolean needsAdditionalInfo = !member.getIsSignedUp();

        return new LoginResult(accessToken, refreshToken, member.getId(), needsAdditionalInfo);
    }

    private Member createMember(UserInfo userInfo) {
        return Member.builder()
                .socialId(userInfo.socialId())
                .socialType(userInfo.socialType())
                .isSignedUp(false)  // 추가 정보 아직 입력 안 했음
                .build();
    }
}