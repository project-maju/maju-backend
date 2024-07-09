package org.duckdns.omaju.api.service.culture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.core.entity.culture.CultureEvent;
import org.duckdns.omaju.core.repository.CultureRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CultureServiceImpl implements CultureService {

    private final CultureRepository cultureRepository;

    @Override
    public DataResponseDTO<List<CultureEvent>> getCultureEvents() {
        List<CultureEvent> events = cultureRepository.findAll();
        System.out.println(events.toString());
        return DataResponseDTO.<List<CultureEvent>>builder()
                .data(events)
                .status(HttpStatus.OK.value())
                .message("문화생활 전체 조회 완료")
                .statusName(HttpStatus.OK.name())
                .build();
    }
}