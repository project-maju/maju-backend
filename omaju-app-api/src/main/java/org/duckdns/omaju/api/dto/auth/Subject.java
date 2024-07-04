package org.duckdns.omaju.api.dto.auth;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.duckdns.omaju.core.entity.member.Member;
import org.duckdns.omaju.core.type.Provider;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subject {

    private String email;
    private String nickname;
    private String profilePath;
    private String type; // 토큰 타입
    private Provider provider;

    @Builder
    private Subject(String email, String nickname, String profilePath, String type, Provider provider) {
        this.email = email;
        this.nickname = nickname;
        this.profilePath = profilePath;
        this.type = type;
        this.provider = provider;
    }

    public static Subject atk(Member member) {
        return Subject.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profilePath(member.getProfileImg())
                .provider(member.getProvider())
                .type("ATK")
                .build();
    }
    public static Subject rtk(Member member) {
        return Subject.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profilePath(member.getProfileImg())
                .provider(member.getProvider())
                .type("RTK")
                .build();
    }
}
