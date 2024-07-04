package org.duckdns.omaju.api.dto.response.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponseDTO {

    @Schema(description = "access 토큰")
    private String accessToken;
    @Schema(description = "refresh 토큰")
    private String refreshToken;
    @Schema(description = "Bearer(토큰 접두어)")
    private String tokenType;
    @Schema(description = "false(boolean type)")
    private Boolean isExist;
    @Schema(description = "false(boolean type)")
    private Boolean isLeft;

    @Builder
    public LoginResponseDTO(String accessToken, String refreshToken, Boolean isExist, Boolean isLeft) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.refreshToken = refreshToken;
        this.isExist = isExist;
        this.isLeft = isLeft;
    }
}
