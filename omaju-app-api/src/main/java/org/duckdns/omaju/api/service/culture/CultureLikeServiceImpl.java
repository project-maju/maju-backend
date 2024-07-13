package org.duckdns.omaju.api.service.culture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.culture.CultureEventDTO;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.core.entity.culture.CultureEvent;
import org.duckdns.omaju.core.entity.culture.CultureLike;
import org.duckdns.omaju.core.entity.member.Member;
import org.duckdns.omaju.core.repository.CultureLikeRepository;
import org.duckdns.omaju.core.repository.CultureRepository;
import org.duckdns.omaju.core.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CultureLikeServiceImpl implements CultureLikeService {

    private final CultureLikeRepository cultureLikeRepository;
    private final MemberRepository memberRepository;
    private final CultureRepository cultureRepository;

    @Override
    public Boolean reverseCultureLike(int memberId, int cultureEventId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다: " + memberId));
        CultureEvent cultureEvent = cultureRepository.findById(cultureEventId)
                .orElseThrow(() -> new RuntimeException("문화 행사를 찾을 수 없습니다: " + cultureEventId));

        Optional<CultureLike> cl = cultureLikeRepository.findByMemberIdAndCultureEventId(member.getId(), cultureEvent.getId());

        if (cl.isPresent()) {
            cultureLikeRepository.delete(cl.get());
            return false;
        } else {
            cultureLikeRepository.save(CultureLike.builder().cultureEvent(cultureEvent).member(member).build());
            return true;
        }
    }

    @Override
    public DataResponseDTO<List<CultureEventDTO>> getCultureLikesByDate(int memberId, LocalDate date) {
        List<CultureEvent> events = cultureLikeRepository.findByMemberIdAndDate(memberId, date);
        List<CultureEventDTO> eventsDTO = events.stream()
                .map(event -> CultureEventDTO.builder()
                        .id(event.getId())
                        .genre(event.getGenre())
                        .category(event.getCategory())
                        .eventName(event.getEventName())
                        .place(event.getPlace())
                        .price(event.getPrice())
                        .url(event.getUrl())
                        .thumbnail(event.getThumbnail())
                        .startDate(event.getStartDate())
                        .endDate(event.getEndDate())
                        .lat(event.getLat())
                        .lon(event.getLon())
                        .build())
                .toList();
        return DataResponseDTO.<List<CultureEventDTO>>builder()
                .data(eventsDTO)
                .status(HttpStatus.OK.value())
                .message("날짜에 따른 문화생활 조회 완료")
                .statusName(HttpStatus.OK.name())
                .build();
    }

    @Override
    public DataResponseDTO<Map<LocalDate, Boolean>> getCultureLikesByMonth(int memberId, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);  // 해당 월의 첫째 날
        LocalDate endDate = yearMonth.atEndOfMonth();  // 해당 월의 마지막 날

        List<CultureEvent> events = cultureLikeRepository.findByMemberIdAndDateRange(memberId, startDate, endDate);

        Map<LocalDate, Boolean> dateMap = new HashMap<>();

        // 해당 월의 모든 날짜를 false로 초기화
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dateMap.put(date, false);
        }

        // 이벤트가 있는 날짜를 true로 설정
        for (CultureEvent event : events) {
            for (LocalDate date = event.getStartDate(); !date.isAfter(event.getEndDate()); date = date.plusDays(1)) {
                if (dateMap.containsKey(date)) {
                    dateMap.put(date, true);
                }
            }
        }
        return DataResponseDTO.<Map<LocalDate, Boolean>>builder()
                .data(dateMap)
                .status(HttpStatus.OK.value())
                .message("날짜에 따른 문화생활 조회 완료")
                .statusName(HttpStatus.OK.name())
                .build();
    }
}