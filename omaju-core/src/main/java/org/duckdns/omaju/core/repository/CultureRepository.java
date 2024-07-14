package org.duckdns.omaju.core.repository;

import org.duckdns.omaju.core.entity.culture.CultureEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CultureRepository extends JpaRepository<CultureEvent, Integer> {

    List<CultureEvent> findTop30ByOrderByIdAsc();

    Optional<CultureEvent> findById(@Param("id") int eventId);

    List<CultureEvent> findTop30ByGenreOrderByIdAsc(@Param("genre") String genre);

    List<CultureEvent> findByGenre(String genre);
}