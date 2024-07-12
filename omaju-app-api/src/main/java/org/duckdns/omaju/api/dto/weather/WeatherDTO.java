package org.duckdns.omaju.api.dto.weather;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeatherDTO {
    @Schema(description = "위도")
    private double lat;

    @Schema(description = "경도")
    private double lon;

    @Schema(description = "현재 기온")
    private double temp;

    @Schema(description = "날씨명")
    private String description;

    @Builder
    public WeatherDTO(double lat, double lon, double temp, String description) {
        this.lat = lat;
        this.lon = lon;
        this.temp = temp;
        this.description = description;
    }
}
