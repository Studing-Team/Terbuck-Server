package com.terbuck.terbuck_be.domain.university.repository;

import com.terbuck.terbuck_be.domain.university.entity.College;
import com.terbuck.terbuck_be.domain.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollegeRepository extends JpaRepository<College, Long> {
    List<College> findByUniversityId(Long universityId);
    boolean existsByNameAndUniversity(String name, University university);
}
