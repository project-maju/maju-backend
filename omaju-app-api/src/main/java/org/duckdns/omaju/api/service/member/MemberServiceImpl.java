package org.duckdns.omaju.api.service.member;

import lombok.RequiredArgsConstructor;
import org.duckdns.omaju.api.dto.auth.MemberDetails;
import org.duckdns.omaju.api.service.jwt.JwtService;
import org.duckdns.omaju.core.entity.member.Member;
import org.duckdns.omaju.core.repository.MemberRepository;
import org.duckdns.omaju.core.type.Provider;
import org.duckdns.omaju.core.util.repository.redis.RedisDao;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final RedisDao redisDao;

    @Override
    public UserDetails loadUserByUsername(String email, Provider provider) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmailAndProvider(email, provider).orElseThrow
                (() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new MemberDetails(member);
    }
}
