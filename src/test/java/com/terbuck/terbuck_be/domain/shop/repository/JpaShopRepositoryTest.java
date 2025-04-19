package com.terbuck.terbuck_be.domain.shop.repository;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.entity.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JpaShopRepositoryTest {

    @Autowired
    JpaShopRepository jpaShopRepository;

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    @Rollback(false)
    void save() {
        // given
        Shop shop = new Shop("업체3", University.용인대학교, ShopCategory.운동,
                new Address("경기도", "용인시", "용인로", "11-5", null),
                "imageURL",
                "shopLinkURL",
                new Location(20.0, 21.5));

        Benefit benefit1 = new Benefit("5% 할인");
        benefit1.changeShop(shop);
        Detail detail1 = new Detail("주류 제외 모든 메뉴 5% 할인 가능");
        detail1.changeBenefit(benefit1);

        Benefit benefit2 = new Benefit("안주 하나 더");
        benefit2.changeShop(shop);
        Detail detail2 = new Detail("2만원 이상 안주 1개");
        Detail detail3 = new Detail("1만원 이하 안주 2개");
        detail2.changeBenefit(benefit2);
        detail3.changeBenefit(benefit2);

        // when
        jpaShopRepository.save(shop);

        em.flush();
        em.clear();

        // then
        List<Shop> all = jpaShopRepository.findAll();
        for (Shop shop1 : all) {
            System.out.println("shop1.getName() = " + shop1.getName());
        }
        ;

        assertThat(jpaShopRepository.findAll().size()).isEqualTo(1);

    }


    @Test
    @Transactional
    void findAll() {
        List<Shop> all = jpaShopRepository.findAll();
        for (Shop shop : all) {
            System.out.println("=================");
            System.out.println("shop.getId() = " + shop.getId());
            System.out.println("shop.getName() = " + shop.getName());
            System.out.println("shop.getUniversity() = " + shop.getUniversity());
            for (Benefit benefit : shop.getBenefitList()) {
                System.out.println("benefit.getName() = " + benefit.getName());
                for (Detail detail : benefit.getDetailList()) {
                    System.out.println("detail.getDescription() = " + detail.getDescription());
                }
            }
            System.out.println("=================");
        }
    }

    @Test
    @Transactional
    void findById() {
        Shop shop = jpaShopRepository.findById(1L);
        System.out.println("=================");
        System.out.println("shop.getId() = " + shop.getId());
        System.out.println("shop.getName() = " + shop.getName());
        System.out.println("shop.getUniversity() = " + shop.getUniversity());
        for (Benefit benefit : shop.getBenefitList()) {
            System.out.println("benefit.getName() = " + benefit.getName());
            for (Detail detail : benefit.getDetailList()) {
                System.out.println("detail.getDescription() = " + detail.getDescription());
            }
        }
    }
}