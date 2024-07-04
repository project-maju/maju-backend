package org.duckdns.omaju.core.entity.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.duckdns.omaju.core.entity.BaseEntity;
import org.duckdns.omaju.core.type.Provider;
import org.duckdns.omaju.core.type.Role;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
@ToString(exclude = "fcmToken")
@NoArgsConstructor
@SQLDelete(sql = "UPDATE member SET IS_LEAVE = true WHERE ID = ?")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(length = 50)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 500)
    private String profileImg;

    @NotNull
    @Column(length = 50)
    private String nickname;

    @Column(length = 200)
    private String fcmToken;

    @NotNull
    private int level = 1;

    @NotNull
    private int exp = 0;

    @NotNull
    private boolean isLeave = Boolean.FALSE;

    @Builder
    public Member(int id, String email, Provider provider, Role role, String profileImg, String nickname, String fcmToken) {
        this.id = id;
        this.email = email;
        this.provider = provider;
        this.role = role;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.fcmToken = fcmToken;
    }
}