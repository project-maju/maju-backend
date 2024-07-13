package org.duckdns.omaju.api.service.walking;

import org.duckdns.omaju.api.dto.response.DataResponseDTO;
import org.duckdns.omaju.core.entity.member.Member;

public interface WalkingService {
    DataResponseDTO<?> walkingTrails(double lat, double lon);
    DataResponseDTO<?> tmapTrace(double startLat, double startLon, double endLat, double endLon);
    DataResponseDTO<?> historyInsert(Member member, double distance, int steps);
}
