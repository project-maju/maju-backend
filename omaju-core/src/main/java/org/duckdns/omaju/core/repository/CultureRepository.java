package org.duckdns.omaju.core.repository;

import org.duckdns.omaju.core.entity.culture.CultureEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CultureRepository extends JpaRepository<CultureEvent, Integer> {

    List<CultureEvent> findAll();
}