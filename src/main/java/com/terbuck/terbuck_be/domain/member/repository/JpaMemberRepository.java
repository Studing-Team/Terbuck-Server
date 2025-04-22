package com.terbuck.terbuck_be.domain.member.repository;

import com.terbuck.terbuck_be.common.enums.SocialType;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.entity.StudentID;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository{

    private final EntityManager em;

    @Override
    @Transactional
    public Long signUp(Member member) {
        em.persist(member);
        return member.getId();
    }

    @Override
    @Transactional
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    @Override
    @Transactional
    public Long changeStudentID(Long id, StudentID studentID) {

        Member findMember = findById(id);
        findMember.changeStudentID(studentID);

        return findMember.getId();
    }

    @Override
    public Optional<Member> findBySocialIdAndSocialType(Long socialId, SocialType socialType) {
        String jpql = "SELECT m FROM Member m WHERE m.socialId = :socialId AND m.socialType = :socialType";
        Member result = em.createQuery(jpql, Member.class)
                .setParameter("socialId", socialId)
                .setParameter("socialType", socialType)
                .getResultStream()
                .findFirst()
                .orElse(null);

        return Optional.ofNullable(result);
    }

}
