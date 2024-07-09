package org.duckdns.omaju.api.config;

import lombok.RequiredArgsConstructor;
import org.duckdns.omaju.api.config.jwt.CustomAuthenticationEntryPoint;
import org.duckdns.omaju.api.service.jwt.JwtService;
import org.duckdns.omaju.api.service.member.MemberService;
import org.duckdns.omaju.core.util.repository.redis.RedisDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppConfig {

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtService jwtService;
    private final MemberService memberService;
    private final RedisDao redisDao;

    // security 설정 활성화
    private static final String[] ALLOWED_ENDPOINT = {
            "/member/kakao-login",
            "/test",
            "/swagger-ui/**",
            "/v3/api-docs/swagger-config",
            "/v3/api-docs",
            "/h2-console",
            "/culture-events"
    };

    /** SecurityFilterChain : Spring Security 필터 체인에서 가장 첫 번째 필터
     * @param http : filterchain을 거치는 요청 http
     * @return http에 authorizedRequests 설정 추가 후 리런. 특정 url 접근 허용
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 필터체인 구성
        http.cors().and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .requestMatchers(ALLOWED_ENDPOINT).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, memberService, redisDao),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
