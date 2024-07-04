package org.duckdns.omaju.core.util.network;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.core.util.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@Getter
@Builder
public class BaseResponse {
    private final Long timestamp = System.currentTimeMillis();
    private final int status;
    private final String statusName;
    private final String message;
    private final Object data;

    public static ResponseEntity<BaseResponse> toResponseEntity(HttpStatus httpStatus, Object o) {
        return toResponseEntity(httpStatus, o, ResponseUtils.SUCCESS);
    }

    public static ResponseEntity<BaseResponse> toResponseEntity(HttpStatus httpStatus, Object o, String message) {
        ResponseEntity<BaseResponse> responseEntity = ResponseEntity
                .status(httpStatus)
                .body(BaseResponse.builder()
                        .status(httpStatus.value())
                        .statusName(httpStatus.name())
                        .message(message)
                        .data(o)
                        .build()
                );

        return responseEntity;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", statusName='" + statusName + '\'' +
                ", message='" + message + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
