package com.terbuck.terbuck_be.domain.infrastructure.fcm;

import com.terbuck.terbuck_be.domain.infrastructure.fcm.service.FcmService;
import com.terbuck.terbuck_be.domain.member.entity.Member;
import com.terbuck.terbuck_be.domain.member.repository.MemberRepository;
import com.terbuck.terbuck_be.domain.shop.entity.Shop;
import com.terbuck.terbuck_be.domain.shop.entity.ShopCategory;
import com.terbuck.terbuck_be.domain.shop.service.ShopService;
import com.terbuck.terbuck_be.domain.university.entity.University;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DailyPushScheduler {

    private final FcmService fcmService;
    private final MemberRepository memberRepository;
    private final ShopService shopService;

    private static final List<PushMessageTemplate> LUNCH_MESSAGES = List.of(
            new PushMessageTemplate(ShopCategory.음식, "오늘 점심, 여기 어때요?", "{매장명}에서 학생증 보여주면 다양한 혜택이 기다리고 있어요!"),
            new PushMessageTemplate(ShopCategory.음식, "점심 메뉴 고민될 땐?", "{매장명}에서 학생증 인증하고 할인된 가격으로 즐겨보세요!"),
            new PushMessageTemplate(ShopCategory.음식, "든든한 한 끼 찾고 있다면?", "{매장명}에서 학생증 혜택으로 알뜰하게 챙기세요!"),
            new PushMessageTemplate(ShopCategory.카페, "커피값 아끼고 싶다면? ☕", "{매장명}에서 학생증 할인 혜택을 받아보세요!"),
            new PushMessageTemplate(ShopCategory.카페, "공부 전 카페인 충전 타임!", "{매장명}에서 학생증 보여주고 더 저렴하게 즐기세요!"),
            new PushMessageTemplate(ShopCategory.카페, "오늘의 당 충전은 여기서 🍰", "{매장명}에서 학생증 혜택으로 달콤한 휴식 챙겨가세요!")
    );

    private static final List<PushMessageTemplate> DINNER_MESSAGES = List.of(
            new PushMessageTemplate(ShopCategory.음식, "오늘 저녁, 특별한 곳을 찾는다면?", "{매장명}에서 친구와 함께 맛있는 시간 보내세요!"),
            new PushMessageTemplate(ShopCategory.주점, "오늘 밤, 스트레스 풀 준비됐나요?", "{매장명}에서 시원한 술 한잔과 함께 하루를 마무리하세요!"),
            new PushMessageTemplate(ShopCategory.음식, "저녁 뭐 먹지? 고민 끝!", "{매장명}에서 학생증 보여주고 든든하게 저녁 해결하세요."),
            new PushMessageTemplate(ShopCategory.주점, "선선한 저녁, 술 한잔 어때요?", "{매장명}에서만 만날 수 있는 특별한 혜택을 확인해보세요.")
    );

    @Scheduled(cron = "0 50 11 * * *", zone = "Asia/Seoul")
    public void sendDailyLunchPush() {
        sendPushNotifications(LUNCH_MESSAGES);
    }

    @Scheduled(cron = "0 30 17 * * *", zone = "Asia/Seoul")
    public void sendDailyDinnerPush() {
        sendPushNotifications(DINNER_MESSAGES);
    }

    private void sendPushNotifications(List<PushMessageTemplate> messages) {
        List<Member> members = memberRepository.findAllWithFcmToken();
        Map<University, List<Member>> membersByUniversity = members.stream()
                .filter(member -> member.getUniversity() != null)
                .collect(Collectors.groupingBy(Member::getUniversity));

        Random random = new Random();

        for (Map.Entry<University, List<Member>> entry : membersByUniversity.entrySet()) {
            University university = entry.getKey();
            List<Member> universityMembers = entry.getValue();

            PushMessageTemplate template = messages.get(random.nextInt(messages.size()));
            Shop randomShop = shopService.findRandomShop(university, template.getCategory());

            if (randomShop != null) {
                String title = template.getTitle();
                String body = template.getContent().replace("{매장명}", randomShop.getName());

                List<String> tokens = universityMembers.stream()
                        .map(Member::getFcmDeviceToken)
                        .collect(Collectors.toList());

                if (!tokens.isEmpty()) {
                    fcmService.sendPush(tokens, title, body);
                }
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    private static class PushMessageTemplate {
        private final ShopCategory category;
        private final String title;
        private final String content;
    }
}
