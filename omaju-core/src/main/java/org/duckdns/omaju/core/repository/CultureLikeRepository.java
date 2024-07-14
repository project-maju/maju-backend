package org.duckdns.omaju.core.repository;

import org.duckdns.omaju.core.entity.culture.CultureEvent;
import org.duckdns.omaju.core.entity.culture.CultureLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CultureLikeRepository extends JpaRepository<CultureLike, Integer> {

    Optional<CultureLike> findByMemberIdAndCultureEventId(int memberId, int cultureEventId);

    @Query("SELECT ce FROM CultureLike cl JOIN cl.cultureEvent ce WHERE cl.member.id = :memberId AND :date BETWEEN ce.startDate AND ce.endDate")
    List<CultureEvent> findByMemberIdAndDate(@Param("memberId") int memberId, @Param("date") LocalDate date);

    @Query("SELECT ce FROM CultureLike cl JOIN cl.cultureEvent ce WHERE cl.member.id = :memberId AND ce.startDate <= :endDate AND ce.endDate >= :startDate")
    List<CultureEvent> findByMemberIdAndDateRange(@Param("memberId") int memberId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}