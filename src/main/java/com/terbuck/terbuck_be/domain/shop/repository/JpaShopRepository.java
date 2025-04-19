package com.terbuck.terbuck_be.domain.shop.repository;

import com.terbuck.terbuck_be.domain.shop.entity.Shop;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                "select s from Shop s"
                , Shop.class
        ).getResultList();
    }

    @Override
    public Shop findById(Long id) {
        return em.find(Shop.class, id);
    }
}
