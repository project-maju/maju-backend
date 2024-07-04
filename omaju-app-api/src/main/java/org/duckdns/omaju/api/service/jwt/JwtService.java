package org.duckdns.omaju.api.service.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.duckdns.omaju.api.dto.auth.LoginResponseDTO;
import org.duckdns.omaju.api.dto.auth.Subject;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.core.entity.member.Member;

public interface JwtService {
    public LoginResponseDTO createTokenByLogin(Member member, boolean isExist) throws JsonProcessingException;
    public Subject extractSubjectFromAtk(String atk) throws JsonProcessingException;
    public DataResponseDTO<?> reissueAtk(Member member) throws JsonProcessingException;
    public Long getExpiration(String atk);

}
