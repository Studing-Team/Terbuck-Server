package com.terbuck.terbuck_be.domain.infrastructure.slack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terbuck.terbuck_be.domain.infrastructure.fcm.service.FcmService;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/slack")
@RequiredArgsConstructor
public class SlackController {

    private final MemberService memberService;
    private final FcmService fcmService;

    @PostMapping("/studentID")
    public ResponseEntity<String> handleSlackAction(@RequestParam("payload") String payload) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);

            var actions = (List<Map<String, Object>>) payloadMap.get("actions");
            var action = actions.get(0);
            String value = (String) action.get("value");  // "1L:true" 또는 "1L:false"

            String[] parts = value.split(":");
            Long userId = Long.valueOf(parts[0]);
            boolean approve = Boolean.parseBoolean(parts[1]);

            log.info("Parsed action - userId: {}, approve: {}", userId, approve);

            if (approve) {
                memberService.enableStudentID(userId);
            } else {
                memberService.rejectStudentID(userId);
            }

            Member member = memberService.findMemberBy(userId);

            String fcmToken = member.getFcmDeviceToken();
            if (fcmToken != null && !fcmToken.isBlank()) {
                String title = "학생증 인증 결과";
                String body = approve ? "학생증 인증이 승인되었습니다." : "학생증 인증이 반려되었습니다.";
                fcmService.sendPush(fcmToken, title, body);
                log.info("푸시 알림 전송 완료: {}", fcmToken);
            } else {
                log.warn("푸시 알림 전송 실패 - FCM 토큰 없음 (userId: {})", userId);
            }

            return ResponseEntity.ok("Action processed successfully");
        } catch (Exception e) {
            log.error("Error processing Slack action", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

}
