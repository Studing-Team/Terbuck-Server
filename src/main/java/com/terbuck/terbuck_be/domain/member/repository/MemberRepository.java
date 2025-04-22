package com.terbuck.terbuck_be.domain.member.repository;


import com.terbuck.terbuck_be.common.enums.SocialType;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.entity.StudentID;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository {

    // 회원 가입
    Long signUp(Member member);

    // 학생증 입력
    Long changeStudentID(Long id, StudentID studentID);

    // ID로 조회
    Member findById(Long id);

    @Transactional
    Optional<Member> findBySocialIdAndSocialType(Long socialId, SocialType socialType);
}
