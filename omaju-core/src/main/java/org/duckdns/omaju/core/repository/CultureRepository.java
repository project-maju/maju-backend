package org.duckdns.omaju.core.repository;

import org.springframework.data.domain.Page;
import org.duckdns.omaju.core.entity.culture.CultureEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CultureRepository extends JpaRepository<CultureEvent, Integer> {

    List<CultureEvent> findTop30ByOrderByIdAsc();

    Optional<CultureEvent> findById(@Param("id") int eventId);

    List<CultureEvent> findTop30ByGenreOrderByIdAsc(@Param("genre") String genre);

    List<CultureEvent> findByGenre(String genre);

    Page<CultureEvent> findAll(Pageable pageable);

    @Query("SELECT ce FROM CultureEvent ce WHERE ce.lat BETWEEN :southLat AND :northLat AND ce.lon BETWEEN :westLon AND :eastLon")
    List<CultureEvent> findAllWithinBounds(@Param("southLat") BigDecimal southLat,
                                           @Param("northLat") BigDecimal northLat,
                                           @Param("westLon") BigDecimal westLon,
                                           @Param("eastLon") BigDecimal eastLon);
}