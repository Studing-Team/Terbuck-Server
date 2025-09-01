package com.terbuck.terbuck_be.domain.university.repository;

import com.terbuck.terbuck_be.domain.university.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByName(String name);
}
