package org.duckdns.omaju.api.dto.response;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DataResponseDTO<T> extends BaseResponseDTO {
    private T data;

    @Builder
    public DataResponseDTO(int status, String statusName, String message, T data) {
        super(status, statusName, message);
        this.data = data;
    }
}
