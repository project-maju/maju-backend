package org.duckdns.omaju.api.service.culture;

import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.core.entity.culture.CultureEvent;

import java.util.List;

public interface CultureService {

    DataResponseDTO<List<CultureEvent>> getCultureEvents();

    DataResponseDTO<CultureEvent> getCultureEventDetail(int eventId);

    DataResponseDTO<List<CultureEvent>> getCultureEventsByGenre(String genre);
}