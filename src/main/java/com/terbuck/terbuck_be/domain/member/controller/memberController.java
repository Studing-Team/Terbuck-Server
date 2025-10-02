package com.terbuck.terbuck_be.domain.member.controller;

import com.terbuck.terbuck_be.common.dto.SuccessMessage;
import com.terbuck.terbuck_be.common.dto.SuccessStatusResponse;
import com.terbuck.terbuck_be.domain.image.service.S3ImageService;
import com.terbuck.terbuck_be.domain.infrastructure.slack.service.SlackService;
import com.terbuck.terbuck_be.domain.member.dto.*;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.service.MemberService;
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
        memberService.deleteMember(userId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(SuccessStatusResponse.of(SuccessMessage.MEMBER_DELETED));
    }


    @PatchMapping("/univ")
    public ResponseEntity<SuccessStatusResponse<?>> updateUniv(
            @RequestBody PatchUnivRequest patchUnivRequest,
            @AuthenticationPrincipal Long userId
    ) {
        memberService.updateUniv(userId, patchUnivRequest.getUniversity());
        memberService.deleteStudentID(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.UNIV_UPDATE_SUCCESS));
    }

    @PatchMapping("/univ/v2")
    public ResponseEntity<SuccessStatusResponse<?>> updateUnivV2(
            @RequestBody @Valid PatchUnivRequestV2 patchUnivRequest,
            @AuthenticationPrincipal Long userId
    ) {
        memberService.updateUnivV2(userId, patchUnivRequest);
        memberService.deleteStudentID(userId);

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

    @PostMapping("/signin/v2")
    public ResponseEntity<SuccessStatusResponse<?>> signInV2(
            @RequestBody @Valid SignInRequestV2 signInRequest,
            @AuthenticationPrincipal Long userId
    ) {
        memberService.signInV2(userId, signInRequest);

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
        memberService.updateStudentID(userId, imageURL, studentNumber, name);

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

    @GetMapping("/studentID/pending")
    public ResponseEntity<SuccessStatusResponse<StudentIDPendingResponse>> getStudentIdPendingStatus(
            @AuthenticationPrincipal Long userId) {
        StudentIDPendingResponse response = memberService.isStudentIdPending(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.STUDENTID_PENDING_STATUS_GET_SUCCESS, response));
    }

}
