package org.duckdns.omaju.api.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.duckdns.omaju.core.type.Provider;

@Getter
@ToString
public class UserInfoDTO {

    private String email;
    private String profileImg;
    private String nickname;
    private Provider provider;
    @Builder
    public UserInfoDTO(String email, String profileImg, String nickname, Provider provider) {
        this.email = email;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.provider = provider;
    }
}