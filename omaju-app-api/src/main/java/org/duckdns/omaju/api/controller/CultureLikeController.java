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
import org.duckdns.omaju.api.service.culture.CultureLikeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Tag(name = "CultureLike", description = "문화행사 북마크 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/culture-like")
public class CultureLikeController {

    private final CultureLikeService cultureLikeService;

    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "북마크를 성공한 경우"), @ApiResponse(responseCode = "400", description = "입력데이터 부적합"), @ApiResponse(responseCode = "401", description = "accessToken 부적합")})
    @Operation(summary = "문화생활 북마크", description = "eventId로 문화생활 글을 북마크합니다.")
    @PostMapping
    public DataResponseDTO<Void> reverseCultureLike(@AuthenticationPrincipal MemberDetails memberDetails, @RequestParam int cultureEventId) {
        Boolean isLiked = cultureLikeService.reverseCultureLike(memberDetails.getMember().getId(), cultureEventId);
        return new DataResponseDTO<>(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(), isLiked.toString(), null);
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공"), @ApiResponse(responseCode = "400", description = "입력 데이터 부적합"), @ApiResponse(responseCode = "401", description = "인증 실패")})
    @Operation(summary = "특정 날짜에 포함된 북마크된 문화생활 조회", description = "특정 날짜가 시작일과 종료일 사이에 포함된 북마크된 문화생활 게시글들을 조회합니다.")
    @GetMapping("/date/{date}")
    public DataResponseDTO<List<CultureEventDTO>> getCultureEventsByDate(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @Parameter(name = "date", description = "yyyy-MM-dd 형식으로 북마크한 문화생활을 조회하고자 하는 날짜 입력") @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return cultureLikeService.getCultureLikesByDate(memberDetails.getMember().getId(), date);
    }

    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "조회 성공"), @ApiResponse(responseCode = "400", description = "입력 데이터 부적합"), @ApiResponse(responseCode = "401", description = "인증 실패")})
    @Operation(summary = "특정 달에 포함된 북마크된 문화생활 조회", description = "특정 달에 포함된 북마크된 문화생활 게시글들을 조회합니다.")
    @GetMapping("/month/{month}")
    public DataResponseDTO<Map<LocalDate, Boolean>> getFavoritesByMonth(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @Parameter(name = "month", description = "yyyy-MM 형식으로 문화생활 북마크 여부를 조회하고자 하는 년/월 입력") @PathVariable String month) {
        YearMonth yearMonth = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyy-MM"));
        return cultureLikeService.getCultureLikesByMonth(memberDetails.getMember().getId(), yearMonth);
    }
}