package org.duckdns.omaju.api.service.culture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.core.entity.culture.CultureEvent;
import org.duckdns.omaju.core.entity.culture.CultureLike;
import org.duckdns.omaju.core.entity.member.Member;
import org.duckdns.omaju.core.repository.CultureLikeRepository;
import org.duckdns.omaju.core.repository.CultureRepository;
import org.duckdns.omaju.core.repository.MemberRepository;
import org.springframework.stereotype.Service;

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

//    @Override
//    public void removeCultureLike(int memberId, int cultureEventId) {
//
//    }
//
//    @Override
//    public DataResponseDTO<List<CultureEventDTO>> getCultureLikes(int memberId) {
//        return null;
//    }
//
//    @Override
//    public DataResponseDTO<List<CultureEventDTO>> getCultureLikesByDate(int memberId, LocalDate date) {
//        return null;
//    }

}
