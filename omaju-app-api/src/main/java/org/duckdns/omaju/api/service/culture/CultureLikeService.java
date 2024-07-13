package org.duckdns.omaju.api.service.culture;

public interface CultureLikeService {

    void addCultureLike(int memberId, int cultureEventId);

//    void removeCultureLike(int memberId, int cultureEventId);
//
//    DataResponseDTO<List<CultureEventDTO>> getCultureLikes(int memberId);
//
//    DataResponseDTO<List<CultureEventDTO>> getCultureLikesByDate(int memberId, LocalDate date);
}
