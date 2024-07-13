package org.duckdns.omaju.api.service.culture;

import org.duckdns.omaju.api.dto.culture.CultureEventDTO;
import org.duckdns.omaju.api.dto.response.DataResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface CultureLikeService {

    Boolean reverseCultureLike(int memberId, int cultureEventId);

    DataResponseDTO<List<CultureEventDTO>> getCultureLikesByDate(int memberId, LocalDate date);
}
