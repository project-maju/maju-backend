package org.duckdns.omaju.api.service.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.auth.LoginResponseDTO;
import org.duckdns.omaju.api.dto.auth.Subject;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.core.entity.member.Member;
import org.duckdns.omaju.core.util.member.MemberUtils;
import org.duckdns.omaju.core.util.repository.redis.RedisDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final ObjectMapper objectMapper;
    private final RedisDao redisDao;

    @Value("${spring.jwt.key}")
    private String key;
    @Value("${spring.jwt.live.atk}")
    private Long atkLive;
    @Value("${spring.jwt.live.rtk}")
    private Long rtkLive;

    @PostConstruct // 스프링 빈이 생성된 후 자동으로 초기화 메서드 실행
    protected void init() {
        // 키값을 암호화하여 저장
        key = Base64.getEncoder().encodeToString(key.getBytes());
    }

    @Override
    public LoginResponseDTO createTokenByLogin(Member member, boolean isExist) throws JsonProcessingException {
        // access token 생성
        String atk = createToken(Subject.atk(member), atkLive);
        String rtk = createToken(Subject.rtk(member), rtkLive);
        // Redis에 refresh token 관리

        redisDao.setValues(MemberUtils.getRedisKeyForMember(member), rtk, Duration.ofMillis(rtkLive));
        return LoginResponseDTO.builder()
                .accessToken(atk)
                .refreshToken(rtk)
                .isExist(isExist)
                .isLeft(member.isLeave())
                .build();
    }

    // 토큰은 발행 유저 정보, 발행 시간, 유효 시간, 그리고 해싱 알고리즘과 키를 설정
    private String createToken(Subject subject, Long tokenLive) throws JsonProcessingException {
        String subjectString = objectMapper.writeValueAsString(subject);
        Claims claims = Jwts.claims()
                .setSubject(subjectString);
        Date date = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenLive))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();
    }

    @Override
    public Subject extractSubjectFromAtk(String atk) throws JsonProcessingException {
        // Jwt 파서에 key 설정 후 토큰 파싱 후 Claim 객체 가져옴
        String subjectStr = Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(atk).getBody()
                .getSubject();
        return objectMapper.readValue(subjectStr, Subject.class);
    }

    @Override
    public DataResponseDTO<?> reissueAtk(Member member) throws JsonProcessingException {
        String rtkInRedis = redisDao.getValues(MemberUtils.getRedisKeyForMember(member));
        LoginResponseDTO tokenResDto;
        if (Objects.isNull(rtkInRedis)){
            tokenResDto = LoginResponseDTO.builder().isExist(true).build();
            return DataResponseDTO.builder()
                    .data(tokenResDto)
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("refresh token이 만료되었습니다.")
                    .statusName(HttpStatus.UNAUTHORIZED.name())
                    .build();
        }

        Subject atkSubject = Subject.atk(member);
        String atk = createToken(atkSubject, atkLive);
        tokenResDto = LoginResponseDTO.builder().accessToken(atk).isExist(true).build();
        return DataResponseDTO.builder()
                .data(tokenResDto)
                .status(HttpStatus.OK.value())
                .message("access token이 재발급 되었습니다.")
                .statusName(HttpStatus.OK.name())
                .build();
    }

    @Override
    public Long getExpiration(String atk) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key.getBytes()).parseClaimsJws(atk);
            Date expiration = claimsJws.getBody().getExpiration();
            return expiration.getTime() - new Date().getTime();
        } catch (SignatureException e) {
            // 서명 검증 실패
            // 예외 처리 또는 오류 메시지 출력
            log.warn("{} - accessToken : {}", e.getMessage(), atk);
            return null; // 또는 적절한 오류 처리 방식을 선택하여 반환
        } catch (NullPointerException e) {
            log.warn("{} - accessToken : {}", e.getMessage(), atk);
            return null;
        } catch (Exception e) {
            // 예외 처리 또는 오류 메시지 출력
            log.warn("{} - accessToken : {}", e.getMessage(), atk);
            return null; // 또는 적절한 오류 처리 방식을 선택하여 반환
        }
    }
}
