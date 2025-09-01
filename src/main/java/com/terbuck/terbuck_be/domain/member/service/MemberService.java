package com.terbuck.terbuck_be.domain.member.service;

import com.terbuck.terbuck_be.common.enums.Role;
import com.terbuck.terbuck_be.common.enums.SocialType;
import com.terbuck.terbuck_be.domain.infrastructure.slack.service.SlackService;
import com.terbuck.terbuck_be.domain.university.entity.University;
import com.terbuck.terbuck_be.domain.university.repository.UniversityRepository;
import com.terbuck.terbuck_be.common.exception.BusinessException;
import com.terbuck.terbuck_be.common.exception.ErrorCode;
import com.terbuck.terbuck_be.domain.auth.dto.UserInfo;
import com.terbuck.terbuck_be.domain.auth.service.KakaoOAuthService;
import com.terbuck.terbuck_be.domain.member.dto.SignInRequest;
import com.terbuck.terbuck_be.domain.member.dto.StudentIDResponse;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.entity.StudentID;
import com.terbuck.terbuck_be.domain.member.repository.JpaMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JpaMemberRepository repository;
    private final KakaoOAuthService kakaoOAuthService;
    private final UniversityRepository universityRepository;
    private final SlackService slackService;

    @Transactional
    public Member findMemberBy(UserInfo userInfo) {
        return repository.findBy(userInfo);
    }

    @Transactional
    public Member findMemberBy(Long id) {
        return repository.findBy(id);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = repository.findBy(id);

        repository.delete(member);
        socialUnlink(member);
    }

    private void socialUnlink(Member member) {
        if ( member.getSocialType() == SocialType.KAKAO){
            kakaoOAuthService.unlink(member.getSocialId());
        }
    }

    @Transactional
    public void signIn(Long userId, SignInRequest signinRequest) {
        Member member = repository.findBy(userId);
        University university = universityRepository.findByName(signinRequest.getUniversity())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNIVERSITY_NOT_FOUND));
        member.additionalInfo(university);

        long memberCount = repository.count();
        slackService.sendMessage("새로운 회원이 가입했습니다! \n현재 총 회원 수: " + memberCount + "명");
    }

    @Transactional
    public Member register(UserInfo userInfo) {
        Member newMember = Member.builder()
                .socialId(userInfo.socialId())
                .role(Role.MEMBER)
                .name(userInfo.name())
                .socialType(userInfo.socialType())
                .isSignedUp(false)
                .studentID(new StudentID(false, null, null))
                .build();

        Long memberId = repository.register(newMember);

        return repository.findBy(memberId);
    }

    @Transactional
    public void updateUniv(Long userId, String universityName) {
        Member member = repository.findBy(userId);
        University university = universityRepository.findByName(universityName)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNIVERSITY_NOT_FOUND));
        member.updateUniversity(university);
    }

    @Transactional
    public StudentIDResponse getStudentID(Long userID) {
        Member member = repository.findBy(userID);

        return StudentIDResponse.of(member);
    }

    @Transactional
    public void updateStudentID(Long userId, String imageURL, String studentNumber) {
        Member member = repository.findBy(userId);
        member.updateStudentID(imageURL, studentNumber);
    }

    @Transactional
    public void deleteStudentID(Long userId) {
        Member member = repository.findBy(userId);
        member.updateStudentID(null, null);
    }

    @Transactional
    public void enableStudentID(Long userId) {
        Member member = repository.findBy(userId);
        member.getStudentID().enable();
    }

    @Transactional
    public void rejectStudentID(Long userId) {
        deleteStudentID(userId);
    }

    @Transactional
    public boolean isRegister(UserInfo userInfo) {
        return repository.findBy(userInfo) != null;
    }
}
