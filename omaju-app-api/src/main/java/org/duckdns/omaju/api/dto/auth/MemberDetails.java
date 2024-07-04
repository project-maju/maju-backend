package org.duckdns.omaju.api.dto.auth;

import lombok.Getter;
import org.duckdns.omaju.core.entity.member.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;


/** Member 정보에 권한 정보 담기
 */
@Getter
public class MemberDetails extends User {

    private final Member member;

    public MemberDetails(Member member) {
        super(member.getEmail(), "", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.member = member;
    }
}