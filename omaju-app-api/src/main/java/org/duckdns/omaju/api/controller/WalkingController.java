package org.duckdns.omaju.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.api.service.walking.WalkingService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Walking", description = "산책")
@RestController
@RequiredArgsConstructor
@RequestMapping("/walking")
public class WalkingController {
    private final WalkingService walkingService;

    @GetMapping("/walking-trails/{lat}/{lon}")
    @Operation(summary = "하, 중, 상 난이도별 산책경로 반환", description = "하, 중, 상 난이도별 산책경로 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 데이터가 조회되는 경우"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 access token 값 입력시 오류"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰으로 요청할 경우(responseBody에서 분기 처리되지 않습니다.)"),
    })
    public DataResponseDTO<?> walkingTrails(
            @Parameter(name = "lat", description = "위도") @PathVariable double lat
            ,@Parameter(name = "lon", description = "경도") @PathVariable double lon) {
        return walkingService.walkingTrails(lat, lon);
    }

    @GetMapping("/tmap-trace/{startLat}/{startLon}/{endLat}/{endLon}")
    @Operation(summary = "T MAP을 이용한 보행자 경로 안내", description = "위도/경도 기반 도보 경로 안내")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 데이터가 조회되는 경우"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 access token 값 입력시 오류"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰으로 요청할 경우(responseBody에서 분기 처리되지 않습니다.)"),
    })
    public DataResponseDTO<?> tmapTrace(
            @Parameter(name = "startLat", description = "시작 위도") @PathVariable double startLat
            ,@Parameter(name = "startLon", description = "시작 경도") @PathVariable double startLon
            ,@Parameter(name = "endLat", description = "종료 위도") @PathVariable double endLat
            ,@Parameter(name = "endLon", description = "종료 경도") @PathVariable double endLon) {
        return walkingService.tmapTrace(startLat, startLon, endLat, endLon);
    }
}
