package com.terbuck.terbuck_be.domain.shop.repository;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.common.exception.BusinessException;
import com.terbuck.terbuck_be.common.exception.ErrorCode;
import com.terbuck.terbuck_be.domain.shop.entity.Location;
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

    @Override
    public List<Shop> findAllByUnivAndCategoryAndLocation(University university, List<ShopCategory> categoryList, Location location) {
        StringBuilder jpql = new StringBuilder("SELECT s FROM Shop s WHERE s.university = :univ");

        if (categoryList != null && !categoryList.isEmpty()) {
            jpql.append(" AND s.category IN :categoryList");
        }

        if (location != null) {
            jpql.append(" ORDER BY POWER(s.location.latitude - :latitude, 2) + POWER(s.location.longitude - :longitude, 2)");
        }

        TypedQuery<Shop> query = em.createQuery(jpql.toString(), Shop.class)
                .setParameter("univ", university);

        if (categoryList != null && !categoryList.isEmpty()) {
            query.setParameter("categoryList", categoryList);
        }

        if (location != null) {
            query.setParameter("latitude", location.getLatitude());
            query.setParameter("longitude", location.getLongitude());
        }

        return query.getResultList();
    }

    @Override
    public Shop findById(Long id) {
        Shop findShop = em.find(Shop.class, id);

        if (findShop == null) {
            throw new BusinessException(ErrorCode.SHOP_NOT_FOUND);
        }
        return findShop;
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
