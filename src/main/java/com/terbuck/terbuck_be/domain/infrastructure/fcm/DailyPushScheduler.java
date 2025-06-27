package com.terbuck.terbuck_be.domain.infrastructure.fcm;

import com.terbuck.terbuck_be.domain.infrastructure.fcm.service.FcmService;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyPushScheduler {

    @Value("${fcm.title}")
    private String title;

    @Value("${fcm.message}")
    private String message;


    private final FcmService fcmService;
    private final MemberRepository memberRepository;

    // 매일 오후 6시
    @Scheduled(cron = "0 0 18 * * *", zone = "Asia/Seoul")
    public void sendDailyPush() {
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            String token = member.getFcmDeviceToken();
            if (token != null && !token.isBlank()) {
                fcmService.sendPush(token, title, message);
            }
        }
    }
}
