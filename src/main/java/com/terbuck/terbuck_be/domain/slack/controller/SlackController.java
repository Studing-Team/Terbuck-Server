package com.terbuck.terbuck_be.domain.slack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public ResponseEntity<String> handleSlackAction(@RequestParam("payload") String payload) {
        try {
            log.info("Slack payload raw received: {}", payload);

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

            return ResponseEntity.ok("Action processed successfully");
        } catch (Exception e) {
            log.error("Error processing Slack action", e);
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

}
