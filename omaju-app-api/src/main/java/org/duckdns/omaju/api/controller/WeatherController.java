package org.duckdns.omaju.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.api.service.weather.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Tag(name = "Weather", description = "날씨")
@RestController
@RequiredArgsConstructor
@RequestMapping("/weather")
@ControllerAdvice
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping("/current-weather")
    @Operation(description = "위도, 경도와 일치하는 지역에 대한 현재 날씨정보 조회")
    @Parameters({@Parameter(name = "lat", description = "날씨를 조회할 지역의 위도")
            ,@Parameter(name = "lon", description = "날씨를 조회할 지역의 경도")
    })
    public DataResponseDTO<?> currentWeather(@RequestParam double lat, @RequestParam double lon) {
        return weatherService.currentWeather(lat, lon);
    }
}
