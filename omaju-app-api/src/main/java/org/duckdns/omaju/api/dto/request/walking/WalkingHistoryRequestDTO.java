package org.duckdns.omaju.api.dto.request.walking;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalkingHistoryRequestDTO {
    @Schema(example = "거리")
    private double distance;

    @Schema(example = "걸음 수")
    private int steps;

    @Builder
    public WalkingHistoryRequestDTO(double distance, int steps) {
        this.distance = distance;
        this.steps = steps;
    }
}
