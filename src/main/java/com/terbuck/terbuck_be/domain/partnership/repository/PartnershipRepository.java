package com.terbuck.terbuck_be.domain.partnership.repository;


import com.terbuck.terbuck_be.domain.university.entity.University;
import com.terbuck.terbuck_be.domain.partnership.entity.Partnership;

public interface PartnershipRepository {

    void save(Partnership partnership);

    Partnership findById(Long id);

    Partnership findByUnivAndName(University university, String partnershipName);
}
