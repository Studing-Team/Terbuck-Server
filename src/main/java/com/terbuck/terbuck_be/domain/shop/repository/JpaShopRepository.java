package com.terbuck.terbuck_be.domain.shop.repository;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.entity.Shop;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaShopRepository implements ShopRepository {

    private final EntityManager em;

    @Override
    public void save(Shop shop) {
        em.persist(shop);
    }

    @Override
    public List<Shop> findAll() {
        return em.createQuery(
                        "select s from Shop s "
                        , Shop.class
                )
                .getResultList();
    }

    @Override
    public List<Shop> findAllByUniv(University university) {
        return em.createQuery(
                        "select s from Shop s where s.university =: univ"
                        , Shop.class
                ).setParameter("univ", university)
                .getResultList();
    }

    @Override
    public Shop findById(Long id) {
        return em.find(Shop.class, id);
    }
}
