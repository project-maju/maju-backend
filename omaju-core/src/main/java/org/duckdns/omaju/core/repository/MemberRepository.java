package org.duckdns.omaju.core.repository;

import org.duckdns.omaju.core.entity.member.Member;
import org.duckdns.omaju.core.type.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findMemberByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByEmailAndProvider(String email, Provider provider);
}