package com.terbuck.terbuck_be.domain.advertise.repository;

import com.terbuck.terbuck_be.domain.advertise.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
}
