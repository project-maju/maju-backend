package org.duckdns.omaju.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.auth.MemberDetails;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.api.service.culture.CultureLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "CultureLike", description = "문화행사 북마크 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/culture-like")
public class CultureLikeController {

    private final CultureLikeService cultureLikeService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "북마크를 성공한 경우"),
            @ApiResponse(responseCode = "400", description = "입력데이터 부적합"),
            @ApiResponse(responseCode = "401", description = "accessToken 부적합")
    })
    @Operation(summary = "문화생활 북마크", description = "eventId로 문화생활 글을 북마크합니다.")
    @PostMapping
    public DataResponseDTO<Void> addFavorite(@AuthenticationPrincipal MemberDetails memberDetails, @RequestParam int cultureEventId) {
        cultureLikeService.addCultureLike(memberDetails.getMember().getId(), cultureEventId);
        return new DataResponseDTO<>(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(), "즐겨찾기 추가 완료", null);
    }
}
