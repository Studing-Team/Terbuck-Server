package com.terbuck.terbuck_be.domain.member.service;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.common.exception.BusinessException;
import com.terbuck.terbuck_be.common.exception.ErrorCode;
import com.terbuck.terbuck_be.domain.auth.dto.UserInfo;
import com.terbuck.terbuck_be.domain.image.service.S3ImageService;
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
    private final S3ImageService imageService;

    @Transactional
    public Member findMemberBy(UserInfo userInfo) {
        return repository.findBy(userInfo);
    }

    @Transactional
    public Member findMemberBy(Long id) {
        return repository.findBy(id);
    }

    @Transactional
    public void signIn(Long userId, SignInRequest signinRequest) {
        Member member = repository.findBy(userId);
        member.additionalInfo(signinRequest);
    }

    @Transactional
    public Member register(UserInfo userInfo) {
        Member newMember = Member.builder()
                .socialId(userInfo.socialId())
                .socialType(userInfo.socialType())
                .isSignedUp(false)
                .studentID(new StudentID(false, null, null))
                .build();

        Long memberId = repository.register(newMember);

        return repository.findBy(memberId);
    }

    @Transactional
    public void updateUniv(Long userId, University university) {
        Member member = repository.findBy(userId);
        member.updateUniversity(university);
    }

    @Transactional
    public StudentIDResponse getStudentID(Long userID) {
        Member member = repository.findBy(userID);
        if (!member.getStudentID().getIsRegistered()) {
            throw new BusinessException(ErrorCode.MEMBER_STUDENTID_NOT_REGISTERED);
        }
        return new StudentIDResponse(member.getName(), member.getStudentID().getStudentNumber(), member.getStudentID().getIdCardImage());
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
