package com.terbuck.terbuck_be.domain.partnership.repository;

import com.terbuck.terbuck_be.domain.partnership.entity.Partnership;
import com.terbuck.terbuck_be.domain.shop.entity.Shop;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaPartnershipRepository implements PartnershipRepository {

    private final EntityManager em;

    @Override
    public void save(Partnership partnership) {
        em.persist(partnership);
    }
}
