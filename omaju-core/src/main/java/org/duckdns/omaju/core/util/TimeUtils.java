package org.duckdns.omaju.core.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class TimeUtils {
    public static final String TIMEZONE_ASIA_SEOUL = "Asia/Seoul";

    public static LocalDateTime toLocalDateTime(long unixTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(unixTime),
                TimeZone.getTimeZone(TIMEZONE_ASIA_SEOUL).toZoneId());
    }

    public static Long toUnixTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.of(TIMEZONE_ASIA_SEOUL)).toEpochSecond();
    }

    public static Boolean hasNHoursElapsed(long pastEpochSeconds, int hours) {
        Instant pastTime = Instant.ofEpochSecond(pastEpochSeconds);
        ZoneId koreaZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime pastTimeKorea = ZonedDateTime.ofInstant(pastTime, koreaZoneId);
        ZonedDateTime nowKorea = ZonedDateTime.now(koreaZoneId);

        long hoursElapsed = ChronoUnit.HOURS.between(pastTimeKorea, nowKorea);

        return hoursElapsed >= hours;
    }
}
