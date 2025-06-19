package com.terbuck.terbuck_be.domain.infrastructure.slack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.terbuck.terbuck_be.common.exception.BusinessException;
import com.terbuck.terbuck_be.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SlackService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${slack.webhook.url}")
    private String slackWebhookUrl;

    public void sendStudentIdUpdateMessage(Long userId, String name, String studentNumber, String socialName, String imageUrl) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("blocks", buildBlocks(userId, name, studentNumber, socialName, imageUrl));

            log.info("payload : {}", payload);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(slackWebhookUrl, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Slack message sent successfully.");
            } else {
                log.error("Failed to send Slack message. Status: {}, Body: {}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("슬랙 메시지 전송 실패 : " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error occurred while sending Slack message.", e);
            throw new BusinessException(ErrorCode.SLACK_MESSAGE_FAILED);
        }
    }

    private List<Map<String, Object>> buildBlocks(Long userId, String name, String studentNumber, String socialName, String imageUrl) {
        List<Map<String, Object>> blocks = new ArrayList<>();

        // [1] 텍스트 블록
        Map<String, Object> textSection = new HashMap<>();
        textSection.put("type", "section");
        textSection.put("text", Map.of(
                "type", "mrkdwn",
                "text", "*이름:* " + name + "\n*학번:* " + studentNumber + "\n*계정 소유자 이름:* " + socialName
        ));
        blocks.add(textSection);

        // [2] 독립형 이미지 블록
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Map<String, Object> imageBlock = new HashMap<>();
            imageBlock.put("type", "image");
            imageBlock.put("image_url", imageUrl);
            imageBlock.put("alt_text", "학생증 이미지");
            blocks.add(imageBlock);
        }

        // [3] 버튼 블록
        Map<String, Object> actionsBlock = new HashMap<>();
        actionsBlock.put("type", "actions");
        actionsBlock.put("elements", List.of(
                Map.of(
                        "type", "button",
                        "text", Map.of("type", "plain_text", "text", "승인"),
                        "style", "primary",
                        "action_id", "approve",
                        "value", userId + ":true"
                ),
                Map.of(
                        "type", "button",
                        "text", Map.of("type", "plain_text", "text", "거절"),
                        "style", "danger",
                        "action_id", "reject",
                        "value", userId + ":false"
                )
        ));
        blocks.add(actionsBlock);

        return blocks;
    }

}

