package org.duckdns.omaju.core.repository;

import org.duckdns.omaju.core.entity.culture.CultureLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CultureLikeRepository extends JpaRepository<CultureLike, Integer> {

//    @Query("SELECT cl FROM culture_like cl WHERE cl.member.id = :memberId AND :date BETWEEN cl.cultureEvent.startDate AND cl.cultureEvent.endDate")
//    List<CultureLike> findByMemberIdAndDate(@Param("memberId") int memberId, @Param("date") LocalDate date);
}
