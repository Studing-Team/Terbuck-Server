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

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class memberController {

    private final MemberService memberService;

    @PostMapping("/signin")
    public ResponseEntity<SuccessStatusResponse<?>> signIn(
            @RequestBody SignInRequest signInRequest,
            @AuthenticationPrincipal Long userId) {

        memberService.signIn(userId, signInRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.SIGN_IN_SUCCESS));
    }

    @PatchMapping("/univ")
    public ResponseEntity<SuccessStatusResponse<?>> updateUniv(
            @RequestParam University university,
            @AuthenticationPrincipal Long userId) {

        memberService.updateUniv(userId, university);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessStatusResponse.of(SuccessMessage.UPDATE_UNIV_SUCCESS));
    }
}
