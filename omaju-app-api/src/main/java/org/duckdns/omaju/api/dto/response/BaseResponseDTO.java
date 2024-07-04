package org.duckdns.omaju.api.dto.response;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseResponseDTO {

    private int status;
    private String statusName;
    private String message;
    private final Long timestamp = System.currentTimeMillis();

    public BaseResponseDTO(int status, String statusName, String message) {
        this.status = status;
        this.statusName = statusName;
        this.message = message;
    }
}