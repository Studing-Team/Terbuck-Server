package com.terbuck.terbuck_be.domain.member.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.domain.image.service.S3ImageService;
import com.terbuck.terbuck_be.domain.member.dto.PatchUnivRequest;
import com.terbuck.terbuck_be.domain.member.dto.SignInRequest;
import com.terbuck.terbuck_be.domain.member.dto.StudentIDResponse;
import com.terbuck.terbuck_be.domain.member.service.MemberService;
import com.terbuck.terbuck_be.domain.infrastructure.slack.service.SlackService;
import jakarta.validation.Valid;
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
    private final S3ImageService imageService;
    private final SlackService slackService;

    @DeleteMapping
    public ResponseEntity<?> deleteMember(
            @AuthenticationPrincipal Long userId
    ) {
        int deletedCount = memberService.deleteMember(userId);
        if (deletedCount > 0) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(SuccessStatusResponse.of(SuccessMessage.MEMBER_DELETED));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
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

    @PostMapping("/signin")
    public ResponseEntity<SuccessStatusResponse<?>> signIn(
            @RequestBody @Valid SignInRequest signInRequest,
            @AuthenticationPrincipal Long userId
    ) {
        memberService.signIn(userId, signInRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.SIGN_IN_SUCCESS));
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
        String imageURL = imageService.uploadStudentIDImage(image);

        memberService.updateStudentID(userId, imageURL, studentNumber);
        String socialName = memberService.findMemberBy(userId).getName();
        slackService.sendStudentIdUpdateMessage(userId, name, studentNumber, socialName, imageURL);

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
