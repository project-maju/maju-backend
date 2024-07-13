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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CultureLikeServiceImpl implements CultureLikeService {

    private final CultureLikeRepository cultureLikeRepository;
    private final MemberRepository memberRepository;
    private final CultureRepository cultureRepository;

    @Override
    public void addCultureLike(int memberId, int cultureEventId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다: " + memberId));
        CultureEvent cultureEvent = cultureRepository.findById(cultureEventId)
                .orElseThrow(() -> new RuntimeException("문화 행사를 찾을 수 없습니다: " + cultureEventId));

        CultureLike favorite = CultureLike.builder()
                .member(member)
                .cultureEvent(cultureEvent)
                .build();

        cultureLikeRepository.save(favorite);
    }

    @Override
    public void removeCultureLike(int memberId, int cultureEventId) {
        cultureLikeRepository.deleteByMemberIdAndCultureEventId(memberId, cultureEventId);
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
}
