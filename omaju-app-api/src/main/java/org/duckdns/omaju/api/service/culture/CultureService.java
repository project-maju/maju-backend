package org.duckdns.omaju.api.service.culture;

import org.duckdns.omaju.api.dto.culture.CultureEventDTO;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.core.entity.member.Member;

import java.util.List;

public interface CultureService {

    DataResponseDTO<List<CultureEventDTO>> getCultureEvents(int memberId);

    DataResponseDTO<CultureEventDTO> getCultureEventDetail(int eventId);

    DataResponseDTO<List<CultureEventDTO>> getCultureEventsByGenre(String genre, int memBerId);
}