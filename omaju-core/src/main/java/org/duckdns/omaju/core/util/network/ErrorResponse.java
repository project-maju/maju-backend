package org.duckdns.omaju.core.util.network;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@Getter
@Builder
public class ErrorResponse {
    private final Long timestamp = System.currentTimeMillis();
    private final int status;
    private final String error;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(HttpStatus httpStatus, Exception e) {
        ResponseEntity<ErrorResponse> responseEntity = ResponseEntity
                .status(httpStatus)
                .body(ErrorResponse.builder()
                        .status(httpStatus.value())
                        .error(httpStatus.name())
                        .message(e.getMessage())
                        .build()
                );

        log.warn("ResponseEntity : {} - toResponseEntity", responseEntity);
        return responseEntity;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
