package org.duckdns.omaju.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.auth.MemberDetails;
import org.duckdns.omaju.api.dto.request.login.LoginRequestDTO;
import org.duckdns.omaju.api.dto.request.logout.LogoutRequestDTO;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.api.service.jwt.JwtService;
import org.duckdns.omaju.api.service.member.MemberService;
import org.duckdns.omaju.core.exeption.RejoinNotAllowedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
@Tag(name = "Member", description = "멤버")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@ControllerAdvice
public class MemberController {
    private final MemberService memberService;
    private final JwtService jwtService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 로그인 됐을 경우"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 access token 값 입력시 오류"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰으로 요청할 경우(responseBody에서 분기 처리되지 않습니다.)"),
    })
    @Operation(summary = "카카오 소셜 로그인", description = "카카오 계정을 이용하여 소셜 로그인을 진행합니다")
    @PostMapping("/kakao-login")
    public DataResponseDTO<?> kakaoLogin(@Valid @RequestBody LoginRequestDTO loginRequestDTO) throws IOException, GeneralSecurityException, JsonProcessingException, RejoinNotAllowedException {
        return memberService.kakaoLogin(loginRequestDTO);
    }

    @Operation(summary = "로그아웃", description = "로그아웃")
    @PostMapping("/logout")
    public DataResponseDTO<?> logout(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @RequestBody LogoutRequestDTO logoutRequestDTO
    )  {
        return memberService.logout(memberDetails.getMember(), logoutRequestDTO);
    }
}
