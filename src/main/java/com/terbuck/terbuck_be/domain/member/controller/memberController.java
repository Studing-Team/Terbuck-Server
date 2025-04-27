package com.terbuck.terbuck_be.domain.member.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.member.dto.PatchUnivRequest;
import com.terbuck.terbuck_be.domain.member.dto.SignInRequest;
import com.terbuck.terbuck_be.domain.member.dto.StudentIDResponse;
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
            @RequestBody PatchUnivRequest patchUnivRequest,
            @AuthenticationPrincipal Long userId
    ) {
        memberService.updateUniv(userId, patchUnivRequest.getUniversity());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.UNIV_UPDATE_SUCCESS));
    }

    @GetMapping("/studentID")
    public ResponseEntity<SuccessStatusResponse<StudentIDResponse>> getStudentID(
            @AuthenticationPrincipal Long userID) {
        StudentIDResponse studentIDResponse = memberService.getStudentID(userID);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.STUDENTID_GET_SUCCESS, studentIDResponse));
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
                .body(SuccessStatusResponse.of(SuccessMessage.STUDENTID_UPDATE_SUCCESS));
    }

    @DeleteMapping("/studentID")
    public ResponseEntity<SuccessStatusResponse<?>> deleteStudentID(
            @AuthenticationPrincipal Long userId
    ) {

        /**
         * TODO : S3에서 이미지 찾아서 삭제하는 로직도 추가하면 좋긴할듯
         */
        memberService.deleteStudentID(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.STUDENTID_DELETE_SUCCESS));
    }

}
