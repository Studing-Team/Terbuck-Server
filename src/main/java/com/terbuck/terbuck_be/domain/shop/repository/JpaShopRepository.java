package com.terbuck.terbuck_be.domain.shop.repository;

import com.terbuck.terbuck_be.domain.university.entity.University;
import com.terbuck.terbuck_be.common.exception.BusinessException;
import com.terbuck.terbuck_be.common.exception.ErrorCode;
import com.terbuck.terbuck_be.domain.shop.entity.Benefit;
import com.terbuck.terbuck_be.domain.shop.entity.Location;
import com.terbuck.terbuck_be.domain.shop.entity.Shop;
import com.terbuck.terbuck_be.domain.shop.entity.ShopCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    public List<Shop> findAllByUniv(University university) {
        return em.createQuery("select s from Shop s where s.university = :university", Shop.class)
                .setParameter("university", university)
                .getResultList();
    }

    @Override
    public List<Shop> findAllByUnivAndCategory(University university, ShopCategory category) {
        return em.createQuery("select s from Shop s where s.university = :university and s.category = :category", Shop.class)
                .setParameter("university", university)
                .setParameter("category", category)
                .getResultList();
    }

    public List<Shop> findAllByUnivAndCategoryAndLocation(University university, List<ShopCategory> categoryList, Location location) {
        // TODO: 원래 코드의 위치 기반 필터링 로직을 알 수 없어, 대학과 카테고리 기준으로만 조회하도록 임시 구현합니다.
        if (categoryList == null || categoryList.isEmpty()) {
            return em.createQuery("select s from Shop s where s.university = :university", Shop.class)
                    .setParameter("university", university)
                    .getResultList();
        }
        return em.createQuery("select s from Shop s where s.university = :university and s.category in :categories", Shop.class)
                .setParameter("university", university)
                .setParameter("categories", categoryList)
                .getResultList();
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

    public List<Benefit> findAllWithDetails(@Param("benefitIds") List<Long> benefitIds){
        return em.createQuery("SELECT DISTINCT b FROM Benefit b LEFT JOIN FETCH b.detailList WHERE b.id IN :benefitIds", Benefit.class)
                .setParameter("benefitIds", benefitIds)
                .getResultList();
    }

}
