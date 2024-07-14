package org.duckdns.omaju.api.service.culture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.culture.CultureEventDTO;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.core.entity.culture.CultureEvent;
import org.duckdns.omaju.core.repository.CultureLikeRepository;
import org.duckdns.omaju.core.repository.CultureRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CultureServiceImpl implements CultureService {

    private final CultureRepository cultureRepository;
    private final CultureLikeRepository cultureLikeRepository;
    private final Random random = new Random();

    private static final Map<String, String> WEATHER_GENRE_MAP = Map.of(
            "흐림", "연극",
            "비", "전시",
            "눈", "음악",
            "맑음", "체험"
    );

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

    @Override
    public DataResponseDTO<CultureEventDTO> getCultureEventByWeather(String weather) {
        String genre = WEATHER_GENRE_MAP.getOrDefault(weather, "체험");

        List<CultureEvent> events = cultureRepository.findByGenre(genre);
        if (events.isEmpty()) {
            throw new RuntimeException("해당 장르의 이벤트가 없습니다.");
        }

        CultureEvent randomEvent = events.get(random.nextInt(events.size()));
        CultureEventDTO randomEventDTO = CultureEventDTO.builder()
                .id(randomEvent.getId())
                .genre(randomEvent.getGenre())
                .category(randomEvent.getCategory())
                .eventName(randomEvent.getEventName())
                .place(randomEvent.getPlace())
                .price(randomEvent.getPrice())
                .url(randomEvent.getUrl())
                .thumbnail(randomEvent.getThumbnail())
                .startDate(randomEvent.getStartDate())
                .endDate(randomEvent.getEndDate())
                .lat(randomEvent.getLat())
                .lon(randomEvent.getLon())
                .build();

        return DataResponseDTO.<CultureEventDTO>builder()
                .data(randomEventDTO)
                .status(HttpStatus.OK.value())
                .message("날씨에 따른 문화생활 조회 완료")
                .statusName(HttpStatus.OK.name())
                .build();
    }

    @Override
    public DataResponseDTO<List<CultureEventDTO>> getCultureEventsPaging(Pageable pageable, int memberId) {
        List<CultureEvent> events = cultureRepository.findAll(pageable).getContent();
        List<CultureEventDTO> eventDTOs = events.stream()
                .map(event -> {
                    boolean likeStatus = isLikedByMember(event, memberId);
                    return new CultureEventDTO(event, likeStatus);})
                .collect(Collectors.toList());

        return DataResponseDTO.<List<CultureEventDTO>>builder()
                .data(eventDTOs)
                .status(HttpStatus.OK.value())
                .message("문화생활 페이징 전체 조회 완료")
                .statusName(HttpStatus.OK.name())
                .build();
    }
}