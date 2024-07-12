package org.duckdns.omaju.api.dto.response.walking;

import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalkingTrailsDTO {
    private int id;
    private String name;
    private String level;
    private double startLat;
    private double startLon;
    private double endLat;
    private double endLon;

    @Builder
    public WalkingTrailsDTO(int id, String name, String level, double startLat, double startLon, double endLat, double endLon) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;
    }
}
