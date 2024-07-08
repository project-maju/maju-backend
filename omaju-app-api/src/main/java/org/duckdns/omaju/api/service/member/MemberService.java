package org.duckdns.omaju.api.service.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.duckdns.omaju.api.dto.request.login.LoginRequestDTO;
import org.duckdns.omaju.api.dto.request.logout.LogoutRequestDTO;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.core.entity.member.Member;
import org.duckdns.omaju.core.type.Provider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface MemberService {
    DataResponseDTO<?> kakaoLogin(LoginRequestDTO loginRequestDTO) throws GeneralSecurityException, IOException, JsonProcessingException;
    UserDetails loadUserByUsername(String email, Provider provider) throws UsernameNotFoundException;
    public DataResponseDTO<?> logout(Member member, LogoutRequestDTO logoutReqDto);
}
