package com.terbuck.terbuck_be.domain.member.service;

import com.terbuck.terbuck_be.common.enums.Role;
import com.terbuck.terbuck_be.common.enums.SocialType;
import com.terbuck.terbuck_be.domain.infrastructure.slack.service.SlackService;
import com.terbuck.terbuck_be.domain.member.dto.SignInRequestV2;
import com.terbuck.terbuck_be.domain.member.dto.StudentIDPendingResponse;
import com.terbuck.terbuck_be.domain.university.entity.College;
import com.terbuck.terbuck_be.domain.university.entity.University;
import com.terbuck.terbuck_be.domain.university.repository.CollegeRepository;
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

    private final JpaMemberRepository memberRepository;
    private final KakaoOAuthService kakaoOAuthService;
    private final UniversityRepository universityRepository;
    private final SlackService slackService;
    private final CollegeRepository collegeRepository;

    @Transactional
    public Member findMemberBy(UserInfo userInfo) {
        return memberRepository.findBy(userInfo);
    }

    @Transactional
    public Member findMemberBy(Long id) {
        return memberRepository.findBy(id);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findBy(id);

        memberRepository.delete(member);
        socialUnlink(member);
    }

    private void socialUnlink(Member member) {
        if ( member.getSocialType() == SocialType.KAKAO){
            kakaoOAuthService.unlink(member.getSocialId());
        }
    }

    @Transactional
    public void signIn(Long userId, SignInRequest signinRequest) {
        Member member = memberRepository.findBy(userId);
        University university = universityRepository.findByName(signinRequest.getUniversity())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNIVERSITY_NOT_FOUND));
        member.additionalInfo(university);

        long memberCount = memberRepository.count();
        slackService.sendMessage("새로운 회원이 가입했습니다! \n현재 총 회원 수: " + memberCount + "명");
    }

    @Transactional
    public void signInV2(Long userId, SignInRequestV2 signinRequest) {
        Member member = memberRepository.findBy(userId);
        University university = universityRepository.findByName(signinRequest.getUniversity())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNIVERSITY_NOT_FOUND));
        College college = collegeRepository.findById(signinRequest.getCollegeId())
                        .orElseThrow(() -> new BusinessException((ErrorCode.COLLEGE_NOT_FOUND)));
        member.additionalInfoV2(university, college);

        long memberCount = memberRepository.count();
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

        Long memberId = memberRepository.register(newMember);

        return memberRepository.findBy(memberId);
    }

    @Transactional
    public void updateUniv(Long userId, String universityName) {
        Member member = memberRepository.findBy(userId);
        University university = universityRepository.findByName(universityName)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNIVERSITY_NOT_FOUND));
        member.updateUniversity(university);
    }

    @Transactional
    public void updateUnivV2(Long userId, com.terbuck.terbuck_be.domain.member.dto.PatchUnivRequestV2 patchUnivRequest) {
        Member member = memberRepository.findBy(userId);
        University university = universityRepository.findByName(patchUnivRequest.getUniversity())
                .orElseThrow(() -> new BusinessException(ErrorCode.UNIVERSITY_NOT_FOUND));
        College college = collegeRepository.findById(patchUnivRequest.getCollegeId())
                .orElseThrow(() -> new BusinessException((ErrorCode.COLLEGE_NOT_FOUND)));
        member.updateUniversityV2(university, college);
    }

    @Transactional
    public StudentIDResponse getStudentID(Long userID) {
        Member member = memberRepository.findBy(userID);

        return StudentIDResponse.of(member);
    }

    @Transactional
    public void updateStudentID(Long userId, String imageURL, String studentNumber, String name) {
        Member member = memberRepository.findBy(userId);
        member.updateStudentID(imageURL, studentNumber);
        slackService.sendStudentIdUpdateMessage(userId, name, studentNumber, member.getName(), imageURL, member.getUniversity());
    }

    @Transactional
    public void deleteStudentID(Long userId) {
        Member member = memberRepository.findBy(userId);
        member.updateStudentID(null, null);
    }

    @Transactional
    public void enableStudentID(Long userId) {
        Member member = memberRepository.findBy(userId);
        member.getStudentID().enable();
    }

    @Transactional
    public void rejectStudentID(Long userId) {
        deleteStudentID(userId);
    }

    @Transactional
    public boolean isRegister(UserInfo userInfo) {
        return memberRepository.findBy(userInfo) != null;
    }

    @Transactional(readOnly = true)
    public StudentIDPendingResponse isStudentIdPending(Long userId) {
        Member member = memberRepository.findBy(userId);
        StudentID studentID = member.getStudentID();

        if (studentID == null || studentID.getIdCardImage() == null || studentID.getIdCardImage().isEmpty()) {
            return StudentIDPendingResponse.of(false);
        }

        boolean isPending = !studentID.getIsRegistered();
        return StudentIDPendingResponse.of(isPending);
    }
}
