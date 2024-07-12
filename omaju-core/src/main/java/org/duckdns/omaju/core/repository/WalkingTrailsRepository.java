package org.duckdns.omaju.core.repository;

import org.duckdns.omaju.core.entity.walking.WalkingTrails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalkingTrailsRepository extends JpaRepository<WalkingTrails, Integer> {
    @Query(value = "SELECT * FROM walking_trails WHERE `level` IN ('쉬움', '매우쉬움') ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<WalkingTrails> findRandomTrailByEasyLevel();

    @Query(value = "SELECT * FROM walking_trails WHERE `level` = '보통' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<WalkingTrails> findRandomTrailByNormalLevel();

    @Query(value = "SELECT * FROM walking_trails WHERE `level` IN ('쉬움', '매우어려움') ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<WalkingTrails> findRandomTrailByHardLevel();
}
