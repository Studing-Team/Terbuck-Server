package com.terbuck.terbuck_be.domain.infrastructure.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.Notification;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmService {

    private static final Logger logger = LoggerFactory.getLogger(FcmService.class);
    private static final int BATCH_SIZE = 100;

    private final MemberRepository repository;

    @Transactional
    public void saveFcmToken(Long memberId, String token) {
        Member member = repository.findBy(memberId);
        member.updateFcmDeviceToken(token);
    }

    public void ManualSendPush(Long id, String title, String body) {
        Member member = repository.findBy(id);
        sendPush(member.getFcmDeviceToken(), title, body);
    }

    public void sendPush(String token, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            logger.info("푸시 성공: {}", response);
        } catch (FirebaseMessagingException e) {
            logger.error("단일 푸시 전송 실패: {}", e.getMessage(), e);
        }
    }

    public void sendPush(List<String> tokens, String title, String body) {
        if (tokens == null || tokens.isEmpty()) {
            logger.warn("토큰 리스트가 비어 있습니다. 푸시 전송을 중단합니다.");
            return;
        }

        int totalSent = 0;
        int totalFailed = 0;

        for (int i = 0; i < tokens.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, tokens.size());
            List<String> batchTokens = tokens.subList(i, end);

            List<Message> messages = batchTokens.stream()
                    .map(token -> Message.builder()
                            .setToken(token)
                            .setNotification(Notification.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .build())
                            .build())
                    .toList();

            try {
                BatchResponse response = FirebaseMessaging.getInstance().sendEach(messages, false);
                totalSent += response.getSuccessCount();
                totalFailed += response.getFailureCount();
                logger.info("배치 전송 완료 ({}~{}): 성공 {}건 / 실패 {}건",
                        i, end - 1, response.getSuccessCount(), response.getFailureCount());
            } catch (FirebaseMessagingException e) {
                logger.error("배치 푸시 전송 실패 ({}~{}): {}", i, end - 1, e.getMessage(), e);
            }
        }

        logger.info("푸시 전송 최종 결과: 성공 {}건 / 실패 {}건 / 총 {}건",
                totalSent, totalFailed, tokens.size());
    }
}
