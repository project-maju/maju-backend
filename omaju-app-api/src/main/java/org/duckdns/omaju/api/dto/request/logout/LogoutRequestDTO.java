package org.duckdns.omaju.api.dto.request.logout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LogoutRequestDTO {
    @Schema(description = "access_token")
    private String accessToken;

    @Schema(example = "refresh_token")
    private String refreshToken;

    @Builder
    public LogoutRequestDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
