package com.terbuck.terbuck_be.domain.partnership.repository;

import com.terbuck.terbuck_be.common.enums.University;
import com.terbuck.terbuck_be.common.exception.BusinessException;
import com.terbuck.terbuck_be.common.exception.ErrorCode;
import com.terbuck.terbuck_be.domain.partnership.entity.Partnership;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaPartnershipRepository implements PartnershipRepository {

    private final static int newLimit = 7;

    private final EntityManager em;

    @Override
    public void save(Partnership partnership) {
        em.persist(partnership);
    }

    public List<Partnership> findAllByUniv(University university) {
        return em.createQuery(
                        "select p from Partnership p where p.university =: univ"
                        , Partnership.class
                ).setParameter("univ", university)
                .getResultList();
    }

    public List<Partnership> findAllNewByUnivAndTime(University university, LocalDateTime today) {
        LocalDateTime sevenDaysAgo = today.minusDays(newLimit);

        return em.createQuery(
                        "select p from Partnership p where p.university = :univ and createdDate >= :sevenDaysAgo"
                        , Partnership.class)
                .setParameter("sevenDaysAgo", sevenDaysAgo)
                .setParameter("univ", university)
                .getResultList();
    }

    @Override
    public Partnership findById(Long id) {
        Partnership findPartnership = em.find(Partnership.class, id);

        if (findPartnership == null) {
            throw new BusinessException(ErrorCode.PARTNERSHIP_NOT_FOUND);
        }
        return findPartnership;
    }

    @Override
    public Partnership findByUnivAndName(University university, String partnershipName) {
        try {
            return em.createQuery("select p from Partnership p where p.university =: univ and p.name =: name",
                            Partnership.class)
                    .setParameter("univ", university)
                    .setParameter("name", partnershipName)
                    .getSingleResult();
        } catch (Exception e) {
            throw new EntityNotFoundException("해당 파트너십을 찾을 수 없습니다.");
        }
    }
}
