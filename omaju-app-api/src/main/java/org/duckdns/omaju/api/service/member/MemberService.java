package org.duckdns.omaju.api.service.member;

import org.duckdns.omaju.core.type.Provider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MemberService {
    UserDetails loadUserByUsername(String email, Provider provider) throws UsernameNotFoundException;
}
