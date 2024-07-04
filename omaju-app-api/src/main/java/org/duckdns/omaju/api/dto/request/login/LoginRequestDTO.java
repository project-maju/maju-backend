package org.duckdns.omaju.api.dto.request.login;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDTO {

    @NotNull
    private String accessToken;

    @NotNull
    private String fcmToken;

    @Builder
    public LoginRequestDTO(String accessToken, String fcmToken) {
        this.accessToken = accessToken;
        this.fcmToken = fcmToken;
    }
}

