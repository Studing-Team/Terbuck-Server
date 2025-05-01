package com.terbuck.terbuck_be.domain.member.repository;

import com.terbuck.terbuck_be.common.enums.SocialType;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.entity.Policy;
import com.terbuck.terbuck_be.domain.member.entity.StudentID;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JpaMemberRepositoryTest {

    @Autowired
    private JpaMemberRepository jpaMemberRepository;

    @Autowired
    private EntityManager em;

    @Disabled
    @Test
    @Rollback
    void register() {
        //given
        Member memberA = new Member(
                1L, SocialType.KAKAO, University.가천대학교,
                new Policy(true, true, true),
                "token");

        //when
        Long savedMemberId = jpaMemberRepository.register(memberA);

        //then
        assertThat(savedMemberId).isEqualTo(1L);
    }

    @Disabled
    @Test
    @Rollback
    void changeStudentID() {
        //given
        Member memberA = new Member(
                1L, SocialType.KAKAO, University.가천대학교,
                new Policy(true, true, true),
                "token");
        Long savedMemberId = jpaMemberRepository.register(memberA);

        //when
        StudentID studentID = new StudentID(false, "student1", "19011702");
        jpaMemberRepository.changeStudentID(savedMemberId, studentID);

        //then
        Member findMember = jpaMemberRepository.findBy(savedMemberId);
        assertThat(findMember.getStudentID().getStudentNumber()).isEqualTo(studentID.getStudentNumber());
    }
}