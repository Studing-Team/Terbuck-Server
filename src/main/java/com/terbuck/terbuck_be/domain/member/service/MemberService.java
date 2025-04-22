package com.terbuck.terbuck_be.domain.member.service;

import com.terbuck.terbuck_be.domain.member.dto.SignInRequest;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.repository.JpaMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final JpaMemberRepository repository;

    public void signIn(Long userId, SignInRequest signinRequest) {
        Member member = repository.findById(userId);
        member.additionalInfo(signinRequest);
    }
}
