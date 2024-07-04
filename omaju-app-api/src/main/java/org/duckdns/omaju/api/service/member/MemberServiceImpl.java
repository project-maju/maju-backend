package org.duckdns.omaju.api.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.auth.MemberDetails;
import org.duckdns.omaju.api.dto.auth.UserDetailsImpl;
import org.duckdns.omaju.api.dto.auth.UserInfoDTO;
import org.duckdns.omaju.api.dto.request.login.LoginRequestDTO;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.api.dto.response.login.LoginResponseDTO;
import org.duckdns.omaju.api.service.jwt.JwtService;
import org.duckdns.omaju.core.entity.member.Member;
import org.duckdns.omaju.core.repository.MemberRepository;
import org.duckdns.omaju.core.type.Provider;
import org.duckdns.omaju.core.type.Role;
import org.duckdns.omaju.core.util.TimeUtils;
import org.duckdns.omaju.core.util.member.MemberUtils;
import org.duckdns.omaju.core.util.repository.redis.RedisDao;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final RedisDao redisDao;

    @Override
    public DataResponseDTO<?> kakaoLogin(LoginRequestDTO loginRequestDTO) throws GeneralSecurityException, IOException, JsonProcessingException {
        Member member = null;
        Boolean isExist;    // 기존 회원 여부
        Boolean isLeft;     // 회원 탈퇴 여부

        // 1. 토큰으로 카카오 API 호출
        UserInfoDTO kakaoUserInfo = getKakaoUserInfo(loginRequestDTO);
        log.info("{} - kakaoUserInfo", kakaoUserInfo);

        // 2. 카카오ID로 회원가입 처리
        Map<String, Object> resMap = signUpMemberIfNeed(kakaoUserInfo, loginRequestDTO);

        member = (Member) resMap.get("member");
        isLeft = (Boolean) resMap.get("isLeft");

        if (isLeft && !TimeUtils.hasNHoursElapsed(member.getUpdatedAt(), 1)) {
            // 재가입이 불가한 경우 (이미 탈퇴한 회원입니다. 탈퇴 후 1시간이 아직 안지난 경우)
            LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
                    .isLeft(true)
                    .isExist(false)
                    .build();
            return DataResponseDTO.builder()
                    .data(loginResponseDTO)
                    .message("탈퇴한 회원입니다. 재가입을 원하는 경우 탈퇴시점으로부터 1시간 후에 다시 시도해주세요.")
                    .statusName(HttpStatus.OK.name())
                    .status(HttpStatus.OK.value())
                    .build();
        }

        if (isLeft && TimeUtils.hasNHoursElapsed(member.getUpdatedAt(), 1)) {
            // 탈퇴한 회원의 마지막 탈퇴 시점으로부터 1시간이 지난 경우
            member.setEmail(member.getEmail() + TimeUtils.toUnixTime(LocalDateTime.now()));
            memberRepository.save(member);
            resMap = signUpMemberIfNeed(kakaoUserInfo, loginRequestDTO);
        }

        member = (Member)resMap.get("member");
        isExist = (Boolean)resMap.get("isExist");

        // 3. 강제 로그인 처리
        Authentication authentication = forceLogin(member);

        // 4. response Header에 JWT 토큰 추가
        LoginResponseDTO token = memberAuthenticationInput(authentication, isExist);
        return DataResponseDTO.builder()
                .data(token)
                .status(HttpStatus.OK.value())
                .message("카카오 로그인이 승인되었습니다.")
                .statusName(HttpStatus.OK.name())
                .build();
    }

    private UserInfoDTO getKakaoUserInfo(LoginRequestDTO loginRequestDTO) throws JsonProcessingException
    {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + loginRequestDTO.getAccessToken());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        // responseBody에 있는 정보를 꺼냄
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        String email = jsonNode.get("kakao_account").get("email").asText();
        String profileImg = jsonNode.get("kakao_account").get("profile").get("profile_image_url").asText();
        String nickname = jsonNode.get("kakao_account").get("profile").get("nickname").asText();

        return UserInfoDTO.builder()
                .email(email)
                .nickname(nickname)
                .profileImg(profileImg)
                .provider(Provider.KAKAO)
                .build();
    }

    /** 3. 카카오, google 계정 email 및 Provider로 회원가입 처리
     *     탈퇴한 유저라면 적절한 응답을 이곳에서 만들어서 반환한다.
     * @param memberInfo : 카카오로부터 받은 user info
     * @return Map<String, Object> : member 정보와 기존에 존재하던 사용자인지 아닌지 여부 정보
     */
    private Map<String, Object> signUpMemberIfNeed(UserInfoDTO memberInfo, LoginRequestDTO loginRequestDTO)
    {
        Map<String, Object> resMap = new HashMap<>();
        boolean isExist = true;
        Member member = memberRepository.findByEmailAndProvider(memberInfo.getEmail(), memberInfo.getProvider())
                .orElse(null);

        // DB에 중복된 User 있는지 확인
        if (Objects.equals(member, null)){
            isExist = false;
            String nickname = getRandomNickName(memberInfo.getNickname());

            member = Member.builder()
                    .email(memberInfo.getEmail())
                    .role(Role.ROLE_USER)
                    .fcmToken(loginRequestDTO.getFcmToken())
                    .profileImg(memberInfo.getProfileImg())
                    .nickname(nickname)
                    .provider(memberInfo.getProvider())
                    .build();
        }

        member.setFcmToken(loginRequestDTO.getFcmToken());
        memberRepository.save(member);


        resMap.put("member", member);
        resMap.put("isExist", isExist);
        resMap.put("isLeft", member.isLeave());

        return resMap;
    }

    /** 4. 강제 로그인 처리
     * @param member : 회원가입처리가 된 사용자 정보
     * @return authentication : 로그인 인증서
     */
    private Authentication forceLogin(Member member)
    {
        UserDetails userDetails = new UserDetailsImpl(member);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    /** 5. response Header에 JWT 토큰 추가
     * @param authentication : 발급된 인증서
     * @param isExist : 기존 사용자 or 신규 사용자 구분 여부
     * @return TokenResDto :
     */
    private LoginResponseDTO memberAuthenticationInput(Authentication authentication, Boolean isExist) throws JsonProcessingException
    {
        // response header token 추가
        UserDetailsImpl userDetailsImpl = ((UserDetailsImpl) authentication.getPrincipal());
        String email = userDetailsImpl.getEmail();
        Provider provider = userDetailsImpl.getProvider();
        return jwtService.createTokenByLogin(memberRepository.findByEmailAndProvider(email, provider).get(), isExist);
    }

    private String getRandomNickName(String nickname) {
        Random random = new Random();

        do {
            nickname = nickname + random.nextInt(9999);
        } while(memberRepository.findByNickname(nickname).isPresent());

        return nickname;
    }

    private void setBlackList(Member member, String accessToken) {
        // refresh token 삭제
        if (redisDao.getValues(MemberUtils.getRedisKeyForMember(member)) != null) {
            redisDao.deleteValues(MemberUtils.getRedisKeyForMember(member));
        }

        // 해당 Access Token 유효시간 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtService.getExpiration(accessToken);

        // 이미 accessToken은 만료된 상태이기 때문에 별도 블랙리스트 처리 x
        if (expiration == null)
            return;

        redisDao.setValues(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    @Override
    public UserDetails loadUserByUsername(String email, Provider provider) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmailAndProvider(email, provider).orElseThrow
                (() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new MemberDetails(member);
    }
}
