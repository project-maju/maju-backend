package org.duckdns.omaju.core.repository;

import org.duckdns.omaju.core.entity.walking.WalkingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalkingHistoryRepository extends JpaRepository<WalkingHistory, Integer> {
}
