package com.terbuck.terbuck_be.domain.university.repository;

import com.terbuck.terbuck_be.domain.university.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University, Long> {

    Optional<University> findByName(String name);
}
