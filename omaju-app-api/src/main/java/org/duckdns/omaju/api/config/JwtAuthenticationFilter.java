package org.duckdns.omaju.api.config;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.auth.Subject;
import org.duckdns.omaju.api.service.jwt.JwtService;
import org.duckdns.omaju.api.service.member.MemberService;
import org.duckdns.omaju.core.util.repository.redis.RedisDao;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final MemberService memberService;
    private final RedisDao redisDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization"); // 헤더에서 Authentication 추출
        if (!Objects.isNull(authorization)) { // 헤더가 존재하는 경우 JWT 토큰 추출

            String atk = authorization.substring(7);
            log.info("authorization : {} - doFilterInternal", authorization);
            /** 블랙리스트 관리 로직
             *  1. 리프레시 토큰은 클라이언트에서 항상 reissue API로 요청하기 떄문에 잘돗된 경로 필터링
             *  2. 로그아웃 시 redis에 로그아웃한 access token을 access token 유효시간 만큼 저장
             *  3. 로그아웃 후 access token을 삭제하지 않고 재사용 시 블랙리스트 필터링 처리
             */
            try {
                Subject subject = jwtService.extractSubjectFromAtk(atk); // JWT 토큰에서 Subject 객체를 추출
                String requestURI = request.getRequestURI();
                // refresh token을 담아서 요청보냈을 때 재발급 url로 접근하지 않는 경우 예외 처리
                if (subject.getType().equals("RTK") && !requestURI.equals("/api/v1/members/reissue")) {
                    throw new JwtException("refresh 토큰으로 접근할 수 없는 URI입니다.");
                }
                String isLogout = redisDao.getValues(atk);
                // redis에 토큰이 없는 경우 정상 처리
                if (ObjectUtils.isEmpty(isLogout)) {
                    UserDetails userDetails = memberService.loadUserByUsername(subject.getEmail(), subject.getProvider());
                    Authentication token = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
                // redis에 토큰이 있는 경우 블랙리스트 예외 처리
                else throw new JwtException("유효하지 않은 access 토큰입니다.");

            } catch (JwtException e) {
                request.setAttribute("exception", e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
