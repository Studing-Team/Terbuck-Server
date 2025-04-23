package com.terbuck.terbuck_be.domain.member.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.member.dto.SignInRequest;
import com.terbuck.terbuck_be.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class memberController {

    private final MemberService memberService;

    @PostMapping("/signin")
    public ResponseEntity<SuccessStatusResponse<?>> signIn(
            @RequestBody SignInRequest signInRequest,
            @AuthenticationPrincipal Long userId
    ) {
        memberService.signIn(userId, signInRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.SIGN_IN_SUCCESS));
    }

    @PatchMapping("/univ")
    public ResponseEntity<SuccessStatusResponse<?>> updateUniv(
            @RequestParam University university,
            @AuthenticationPrincipal Long userId
    ) {
        memberService.updateUniv(userId, university);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.UPDATE_UNIV_SUCCESS));
    }

    @PutMapping("/studentID")
    public ResponseEntity<SuccessStatusResponse<?>> updateStudentID(
            @RequestParam MultipartFile image,
            @RequestPart String name,
            @RequestPart String studentNumber,
            @AuthenticationPrincipal Long userId
    ) {
        /**
         * TODO : 요청 들어오면 슬랙 메시지 생성해서 보내도록 한다. (이미지, 이름, 학번 , 소셜 로그인 이름)
         */
        memberService.updateStudentID(userId, image, studentNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.UPDATE_STUDENTID_SUCCESS));
    }
}
