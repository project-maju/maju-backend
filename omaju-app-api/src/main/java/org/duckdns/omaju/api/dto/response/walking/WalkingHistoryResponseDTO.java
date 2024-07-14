package org.duckdns.omaju.api.dto.response.walking;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalkingHistoryResponseDTO {
    private int id;
    private double distance;
    private int steps;

    @Builder
    public WalkingHistoryResponseDTO(int id, double distance, int steps) {
        this.id = id;
        this.distance = distance;
        this.steps = steps;
    }
}
