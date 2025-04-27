package com.terbuck.terbuck_be.domain.shop.repository;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.domain.shop.entity.Shop;
import com.terbuck.terbuck_be.domain.shop.entity.ShopCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
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

    public List<Shop> findAllByUnivAndCategory(University university, List<ShopCategory> categoryList) {
        String jpql = "SELECT s FROM Shop s WHERE s.university = :univ";

        if (categoryList != null && !categoryList.isEmpty()) {
            jpql += " AND s.category IN :categoryList";
        }

        TypedQuery<Shop> query = em.createQuery(jpql, Shop.class)
                .setParameter("univ", university);

        if (categoryList != null && !categoryList.isEmpty()) {
            query.setParameter("categoryList", categoryList);
        }

        return query.getResultList();
    }

    @Override
    public Shop findById(Long id) {
        return em.find(Shop.class, id);
    }

    @Override
    public Shop findByUnivAndName(University university, String name) {
        try {
            return em.createQuery("select s from Shop s where s.university = :univ and s.name = :name", Shop.class)
                    .setParameter("univ", university)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("해당 업체를 찾을 수 없습니다.");
        }
    }

}
