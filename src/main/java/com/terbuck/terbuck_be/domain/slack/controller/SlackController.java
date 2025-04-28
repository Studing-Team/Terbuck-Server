package com.terbuck.terbuck_be.domain.slack.controller;

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

    @PostMapping("/studentID")
    public ResponseEntity<String> handleSlackAction(@RequestBody Map<String, Object> payload) {
        try {
            log.info("Slack payload received: {}", payload);

            // payload 파싱
            var actions = (List<Map<String, Object>>) payload.get("actions");
            var action = actions.get(0);
            String value = (String) action.get("value");  // "1L:true" 또는 "1L:false"

            String[] parts = value.split(":");
            Long userId = Long.valueOf(parts[0]);
            boolean approve = Boolean.parseBoolean(parts[1]);

            log.info("Parsed action - userId: {}, approve: {}", userId, approve);

            // TODO: 이 userId와 approve를 가지고 원하는 로직 처리하면 됨.
            if (approve) {
                memberService.enableStudentID(userId);
            }else{
                memberService.rejectStudentID(userId);
            }

            return ResponseEntity.ok("Action processed successfully");
        } catch (Exception e) {
            log.error("Error processing Slack action", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}
