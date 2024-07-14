package org.duckdns.omaju.api.service.walking;

import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.core.entity.member.Member;

import java.time.LocalDate;
import java.time.YearMonth;

public interface WalkingService {
    DataResponseDTO<?> walkingTrails(double lat, double lon);
    DataResponseDTO<?> tmapTrace(double startLat, double startLon, double endLat, double endLon);
    DataResponseDTO<?> historyInsert(Member member, double distance, int steps);
    DataResponseDTO<?> walkingHistoryByDate(Member member, LocalDate date);
    DataResponseDTO<?> walkingHistoryByMonth(Member member, YearMonth yearMonth);
}
