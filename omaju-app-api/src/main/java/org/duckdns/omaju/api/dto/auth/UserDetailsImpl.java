package org.duckdns.omaju.api.dto.auth;

import org.duckdns.omaju.core.entity.member.Member;
import org.duckdns.omaju.core.type.Provider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    String nickname;
    String email;
    String profileImg;
    Provider provider;

    public UserDetailsImpl(Member member) {
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.profileImg = member.getProfileImg();
        this.provider = member.getProvider();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(this.provider.toString()));
        return auth;
    }
    public String getEmail(){
        return email;
    }
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public Provider getProvider() {
        return this.provider;
    }
}
