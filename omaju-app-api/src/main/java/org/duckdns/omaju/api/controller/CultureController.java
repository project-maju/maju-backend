package org.duckdns.omaju.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.auth.MemberDetails;
import org.duckdns.omaju.api.dto.culture.CultureEventDTO;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.api.dto.weather.WeatherResponseDTO;
import org.duckdns.omaju.api.service.culture.CultureService;
import org.duckdns.omaju.api.service.weather.WeatherService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "CulturalEvent", description = "문화행사 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/culture-events")
public class CultureController {

    private final CultureService cultureService;
    private final WeatherService weatherService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 문화행사를 조회한 경우"),
            @ApiResponse(responseCode = "404", description = "문화행사를 조회하지 못한 경우")
    })
    @Operation(summary = "모든 문화행사 조회", description = "저장된 모든 문화행사 데이터를 조회합니다.")
    @GetMapping("/list")
    public DataResponseDTO<List<CultureEventDTO>> getCultureEvents(@AuthenticationPrincipal MemberDetails memberDetails) {
        return cultureService.getCultureEvents(memberDetails.getMember().getId());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 문화행사 디테일을 조회한 경우"),
            @ApiResponse(responseCode = "404", description = "문화행사 디테일을 조회하지 못한 경우")
    })
    @Operation(summary = "특정 문화행사 디테일 조회", description = "eventId로 특정 문화행사 디테일을 조회합니다.")
    @GetMapping("/detail/{eventId}")
    public DataResponseDTO<CultureEventDTO> getCultureEventDetail(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable int eventId) {
        return cultureService.getCultureEventDetail(eventId, memberDetails.getMember().getId());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 문화행사를 조회한 경우"),
            @ApiResponse(responseCode = "404", description = "문화행사를 조회하지 못한 경우")
    })
    @Operation(summary = "특정 장르 문화행사 조회", description = "장르에 따른 문화행사 데이터를 조회합니다.")
    @GetMapping("/list/{genre}")
    public DataResponseDTO<List<CultureEventDTO>> getCultureEventsByGenre(@PathVariable String genre, @AuthenticationPrincipal MemberDetails memberDetails) {
        return cultureService.getCultureEventsByGenre(genre, memberDetails.getMember().getId());
    }

    @GetMapping("/home-recommendation/{lat}/{lon}")
    @Operation(summary = "날씨에 따른 문화행사 추천", description = "날씨에 따른 문화행사 데이터를 한 가지 추천합니다.")
    public DataResponseDTO<CultureEventDTO> getCultureEventByWeather(
            @Parameter(name = "lat", description = "주소를 조회할 지역의 위도") @PathVariable double lat,
            @Parameter(name = "lon", description = "주소를 조회할 지역의 경도") @PathVariable double lon) {
        String weather = "맑음";
        DataResponseDTO<?> response = weatherService.currentWeather(lat, lon);
        if (response.getData() instanceof WeatherResponseDTO) {
            weather = ((WeatherResponseDTO) response.getData()).getDescription();
        }
        return cultureService.getCultureEventByWeather(weather);
    }
}