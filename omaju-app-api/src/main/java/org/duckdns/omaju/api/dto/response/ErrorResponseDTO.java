package org.duckdns.omaju.api.dto.response;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponseDTO extends BaseResponseDTO{

    @Builder
    public ErrorResponseDTO(int status, String statusName, String message) {
        super(status, statusName, message);
    }
}
