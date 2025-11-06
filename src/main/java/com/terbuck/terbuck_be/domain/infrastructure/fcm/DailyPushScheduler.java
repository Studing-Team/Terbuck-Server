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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyPushScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DailyPushScheduler.class);

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

    @Transactional(readOnly = true)
    @Scheduled(cron = "0 50 11 * * *", zone = "Asia/Seoul")
    public void sendDailyLunchPush() {
        logger.info("Daily Lunch Push scheduled task started.");
        sendPushNotifications(LUNCH_MESSAGES);
        logger.info("Daily Lunch Push scheduled task finished.");
    }

    @Transactional(readOnly = true)
    @Scheduled(cron = "0 30 17 * * *", zone = "Asia/Seoul")
    public void sendDailyDinnerPush() {
        logger.info("Daily Dinner Push scheduled task started.");
        sendPushNotifications(DINNER_MESSAGES);
        logger.info("Daily Dinner Push scheduled task finished.");
    }

    private void sendPushNotifications(List<PushMessageTemplate> messages) {
        logger.info("Fetching members with FCM tokens...");
        List<Member> members = memberRepository.findAllWithFcmToken();
        logger.info("Found {} members with FCM tokens.", members.size());

        Map<University, List<Member>> membersByUniversity = members.stream()
                .filter(member -> member.getUniversity() != null)
                .collect(Collectors.groupingBy(Member::getUniversity));
        logger.info("Grouped members by {} universities.", membersByUniversity.size());

        Random random = new Random();

        for (Map.Entry<University, List<Member>> entry : membersByUniversity.entrySet()) {
            University university = entry.getKey();
            List<Member> universityMembers = entry.getValue();
            logger.info("Processing university: {} with {} members.", university.getName(), universityMembers.size());

            PushMessageTemplate template = messages.get(random.nextInt(messages.size()));
            logger.info("Selected push message template for category: {}.", template.getCategory());

            Shop randomShop = shopService.findRandomShop(university, template.getCategory());

            if (randomShop != null) {
                logger.info("Found random shop: {} for university: {} and category: {}.", randomShop.getName(), university.getName(), template.getCategory());
                String title = template.getTitle();
                String body = template.getContent().replace("{매장명}", randomShop.getName());

                List<String> tokens = universityMembers.stream()
                        .map(Member::getFcmDeviceToken)
                        .collect(Collectors.toList());

                if (!tokens.isEmpty()) {
                    logger.info("Sending push notification to {} tokens for university: {}.\nTitle: {}, Body: {}", tokens.size(), university.getName(), title, body);
                    fcmService.sendPush(tokens, title, body);
                } else {
                    logger.warn("No FCM tokens found for university: {} to send push notification.", university.getName());
                }
            } else {
                logger.warn("No random shop found for university: {} and category: {}. Skipping push notification for this university.", university.getName(), template.getCategory());
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
