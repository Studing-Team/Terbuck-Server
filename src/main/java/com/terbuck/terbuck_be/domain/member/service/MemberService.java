package com.terbuck.terbuck_be.domain.member.service;

import com.terbuck.terbuck_be.common.enums.University;
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

    public void signIn(Long userId, SignInRequest signinRequest) {
        Member member = repository.findById(userId);
        member.additionalInfo(signinRequest);
    }

    public void updateUniv(Long userId, University university) {
        Member member = repository.findById(userId);
        member.updateUniversity(university);
    }

    public StudentIDResponse getStudentID(Long userID) {
        Member member = repository.findById(userID);
        if (!member.getStudentID().getIsRegistered()) {
            throw new IllegalArgumentException("아직 등록이 완료되지 않은 학생증입니다.");
        }
        return new StudentIDResponse(member.getName(), member.getStudentID().getStudentNumber(), member.getStudentID().getIdCardImage());
    }

    public void updateStudentID(Long userId, MultipartFile studentIDImage, String studentNumber) {
        Member member = repository.findById(userId);
        try {
            String imageURL = imageService.uploadStudentIDImage(studentIDImage);
            member.updateStudentID(imageURL, studentNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteStudentID(Long userId) {
        Member member = repository.findById(userId);
        member.updateStudentID(null, null);
    }
}
