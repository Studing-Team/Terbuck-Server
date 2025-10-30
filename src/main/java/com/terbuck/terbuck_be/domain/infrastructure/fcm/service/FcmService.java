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
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(notification)
                .build();

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            logger.info("멀티캐스트 푸시 성공: {}", response);
        } catch (FirebaseMessagingException e) {
            logger.error("멀티캐스트 푸시 전송 실패: {}", e.getMessage(), e);
        }
    }
}
