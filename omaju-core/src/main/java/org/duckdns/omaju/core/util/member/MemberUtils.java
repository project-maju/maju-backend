package org.duckdns.omaju.core.util.member;

import org.duckdns.omaju.core.entity.member.Member;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class MemberUtils {

    public static Boolean hasNHoursElapsed(long pastEpochSeconds, int hours) {
        Instant pastTime = Instant.ofEpochSecond(pastEpochSeconds);
        ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime pastTimeKorea = ZonedDateTime.ofInstant(pastTime, koreaZoneId);
        ZonedDateTime nowKorea = ZonedDateTime.now(koreaZoneId);

        long hoursElapsed = ChronoUnit.HOURS.between(pastTimeKorea, nowKorea);

        return hoursElapsed >= hours;
    }

    public static String getRedisKeyForMember(Member member) {
        return member.getEmail() + " " + member.getProvider();
    }
}
