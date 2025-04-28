package com.terbuck.terbuck_be.domain.member.service;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.auth.dto.UserInfo;
import com.terbuck.terbuck_be.domain.image.service.S3ImageService;
import com.terbuck.terbuck_be.domain.member.dto.SignInRequest;
import com.terbuck.terbuck_be.domain.member.dto.StudentIDResponse;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.repository.JpaMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final JpaMemberRepository repository;
    private final S3ImageService imageService;

    public Member findMemberBy(UserInfo userInfo) {
        return repository.findBy(userInfo);
    }

    public Member findMemberBy(Long id) {
        return repository.findBy(id);
    }

    public void signIn(Long userId, SignInRequest signinRequest) {
        Member member = repository.findBy(userId);
        member.additionalInfo(signinRequest);
    }

    public Member register(UserInfo userInfo) {
        Member newMember = Member.builder()
                .socialId(userInfo.socialId())
                .socialType(userInfo.socialType())
                .isSignedUp(false)  // 추가 정보 아직 입력 안 했음
                .build();

        Long memberId = repository.signUp(newMember);

        return repository.findBy(memberId);
    }

    public void updateUniv(Long userId, University university) {
        Member member = repository.findBy(userId);
        member.updateUniversity(university);
    }

    public StudentIDResponse getStudentID(Long userID) {
        Member member = repository.findBy(userID);
        if (!member.getStudentID().getIsRegistered()) {
            throw new IllegalArgumentException("아직 등록이 완료되지 않은 학생증입니다.");
        }
        return new StudentIDResponse(member.getName(), member.getStudentID().getStudentNumber(), member.getStudentID().getIdCardImage());
    }

    public void updateStudentID(Long userId, String imageURL, String studentNumber) {
        Member member = repository.findBy(userId);
        member.updateStudentID(imageURL, studentNumber);
    }

    public void deleteStudentID(Long userId) {
        Member member = repository.findBy(userId);
        member.updateStudentID(null, null);
    }

    public void enableStudentID(Long userId) {
        Member member = repository.findBy(userId);
        member.getStudentID().enable();
    }

    public void rejectStudentID(Long userId) {
        deleteStudentID(userId);
    }

    public boolean isRegister(UserInfo userInfo) {
        return repository.findBy(userInfo) != null;
    }
}
