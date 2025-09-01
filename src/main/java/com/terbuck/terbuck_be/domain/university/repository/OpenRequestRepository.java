package com.terbuck.terbuck_be.domain.university.repository;

import com.terbuck.terbuck_be.domain.university.entity.OpenRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpenRequestRepository extends JpaRepository<OpenRequest, Long> {
    Optional<OpenRequest> findByUniversityNameAndMemberId(String universityName, Long memberId);

    Optional<OpenRequest> findByMemberId(Long memberId);
}
