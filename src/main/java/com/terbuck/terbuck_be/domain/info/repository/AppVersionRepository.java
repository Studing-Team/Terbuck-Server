package com.terbuck.terbuck_be.domain.info.repository;

import com.terbuck.terbuck_be.domain.info.entity.AppVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
    Optional<AppVersion> findByOs(AppVersion.OS os);
}
