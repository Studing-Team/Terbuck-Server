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
    @Scheduled(cron = "0 30 17 * * *", zone = "Asia/Seoul")
    public void sendDailyDinnerPush() {
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            String token = member.getFcmDeviceToken();
            if (token != null && !token.isBlank()) {
                fcmService.sendPush(token, "오늘 하루도 고생했어요 ☘" , "제휴 혜택 놓치지 말고 지금 바로 확인하세요 💡");
            }
        }
    }

    @Scheduled(cron = "0 50 11 * * *", zone = "Asia/Seoul")
    public void sendDailyLunchPush() {
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            String token = member.getFcmDeviceToken();
            if (token != null && !token.isBlank()) {
                fcmService.sendPush(token, "배고픈 점심 시간이에요 🍙", "지금 학교 근처 제휴 혜택 확인하고 할인 챙겨가세요!");
            }
        }
    }
}
