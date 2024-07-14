package org.duckdns.omaju.api.service.culture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.culture.CultureEventDTO;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.core.entity.culture.CultureEvent;
import org.duckdns.omaju.core.repository.CultureLikeRepository;
import org.duckdns.omaju.core.repository.CultureRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CultureServiceImpl implements CultureService {

    private final CultureRepository cultureRepository;
    private final CultureLikeRepository cultureLikeRepository;

    @Override
    public DataResponseDTO<List<CultureEventDTO>> getCultureEvents(int memberId) {
        List<CultureEvent> events = cultureRepository.findTop30ByOrderByIdAsc();
        List<CultureEventDTO> eventDTOs = events.stream()
                .map(event -> {
                    boolean likeStatus = isLikedByMember(event, memberId);
                    return new CultureEventDTO(event, likeStatus);})
                .collect(Collectors.toList());

        return DataResponseDTO.<List<CultureEventDTO>>builder()
                .data(eventDTOs)
                .status(HttpStatus.OK.value())
                .message("문화생활 전체 조회 완료")
                .statusName(HttpStatus.OK.name())
                .build();
    }

    private boolean isLikedByMember(CultureEvent event, int memberId) {
        return cultureLikeRepository.findByMemberIdAndCultureEventId(memberId, event.getId()).isPresent();
    }

    @Override
    public DataResponseDTO<CultureEventDTO> getCultureEventDetail(int eventId, int memberId) {
        CultureEvent event = cultureRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("문화행사를 찾을 수 없습니다: " + eventId));
        boolean likeStatus = isLikedByMember(event, memberId);
        CultureEventDTO cultureEventDTO = new CultureEventDTO(event, likeStatus);

        return DataResponseDTO.<CultureEventDTO>builder()
                .data(cultureEventDTO)
                .status(HttpStatus.OK.value())
                .message("문화생활 상세 조회 완료")
                .statusName(HttpStatus.OK.name())
                .build();
    }

    @Override
    public DataResponseDTO<List<CultureEventDTO>> getCultureEventsByGenre(String genre, int memberId) {
        List<CultureEvent> events = cultureRepository.findTop30ByGenreOrderByIdAsc(genre);

        List<CultureEventDTO> eventDTOs = events.stream()
                .map(event -> {
                    boolean likeStatus = isLikedByMember(event, memberId);
                    return new CultureEventDTO(event, likeStatus);})
                .collect(Collectors.toList());

        return DataResponseDTO.<List<CultureEventDTO>>builder()
                .data(eventDTOs)
                .status(HttpStatus.OK.value())
                .message("장르별 문화생활 조회 완료")
                .statusName(HttpStatus.OK.name())
                .build();
    }
}