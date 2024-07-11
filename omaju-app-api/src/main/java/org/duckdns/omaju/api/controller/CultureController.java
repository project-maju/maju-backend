package org.duckdns.omaju.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.api.service.culture.CultureService;
import org.duckdns.omaju.core.entity.culture.CultureEvent;
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 문화행사를 조회한 경우"),
            @ApiResponse(responseCode = "404", description = "문화행사를 조회하지 못한 경우")
    })
    @Operation(summary = "모든 문화행사 조회", description = "저장된 모든 문화행사 데이터를 조회합니다.")
    @GetMapping("/list")
    public DataResponseDTO<List<CultureEvent>> getCultureEvents() {
        return cultureService.getCultureEvents();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 문화행사 디테일을 조회한 경우"),
            @ApiResponse(responseCode = "404", description = "문화행사 디테일을 조회하지 못한 경우")
    })
    @Operation(summary = "특정 문화행사 디테일 조회", description = "eventId로 특정 문화행사 디테일을 조회합니다.")
    @GetMapping("/detail/{eventId}")
    public DataResponseDTO<CultureEvent> getCultureEventDetail(@PathVariable int eventId) {
        return cultureService.getCultureEventDetail(eventId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "정상적으로 문화행사를 조회한 경우"),
            @ApiResponse(responseCode = "404", description = "문화행사를 조회하지 못한 경우")
    })
    @Operation(summary = "특정 장르 문화행사 조회", description = "장르에 따른 문화행사 데이터를 조회합니다.")
    @GetMapping("/list/{genre}")
    public DataResponseDTO<List<CultureEvent>> getCultureEventsByGenre(@PathVariable String genre) {
        return cultureService.getCultureEventsByGenre(genre);
    }
}