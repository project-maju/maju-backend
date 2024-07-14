package org.duckdns.omaju.api.service.culture;

import org.duckdns.omaju.api.dto.culture.CultureEventDTO;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.math.BigDecimal;

public interface CultureService {

    DataResponseDTO<List<CultureEventDTO>> getCultureEvents(int memberId);

    DataResponseDTO<CultureEventDTO> getCultureEventDetail(int eventId, int memberId);

    DataResponseDTO<List<CultureEventDTO>> getCultureEventsByGenre(String genre, int memBerId);

    DataResponseDTO<CultureEventDTO> getCultureEventByWeather(String weather);

    DataResponseDTO<List<CultureEventDTO>> getCultureEventsPaging(Pageable pageable, int memberId);

    DataResponseDTO<List<CultureEventDTO>> getCultureEventsWithinBounds(BigDecimal southLat, BigDecimal northLat, BigDecimal westLon, BigDecimal eastLon, int memberId);
}