package com.terbuck.terbuck_be.domain.member.repository;

import com.terbuck.terbuck_be.common.exception.BusinessException;
import com.terbuck.terbuck_be.common.exception.ErrorCode;
import com.terbuck.terbuck_be.domain.auth.dto.UserInfo;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.entity.StudentID;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    @Override
    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
    }

    @Override
    public Member findBy(Long id) {
        Member member = em.find(Member.class, id);

        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
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

    public void delete(Member member) {
        Member managedMember = em.contains(member) ? member : em.merge(member);
        em.remove(managedMember);
    }

    @Override
    public Long register(Member member) {
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
