package com.terbuck.terbuck_be.domain.member.repository;

import com.terbuck.terbuck_be.common.enums.SocialType;
import com.terbuck.terbuck_be.domain.auth.dto.UserInfo;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.entity.StudentID;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    @Override
    public Member findBy(Long id) {
        Member member = em.find(Member.class, id);

        if (member == null) {
            throw new EntityNotFoundException("id 를 통한 엔티티 조회에 실패했습니다.");
        }

        return member;
    }

    @Override
    public Member findBy(UserInfo userInfo) {
        String jpql = "SELECT m FROM Member m WHERE m.socialId = :socialId AND m.socialType = :socialType";
        Member member = em.createQuery(jpql, Member.class)
                .setParameter("socialId", userInfo.socialId())
                .setParameter("socialType", userInfo.socialType())
                .getResultStream()
                .findFirst()
                .orElse(null);

        return member;
    }

    @Override
    public Long signUp(Member member) {
        em.persist(member);
        return member.getId();
    }

    @Override
    public Long changeStudentID(Long id, StudentID studentID) {

        Member findMember = findBy(id);
        findMember.changeStudentID(studentID);

        return findMember.getId();
    }
}
