package org.duckdns.omaju.api.dto.culture;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.duckdns.omaju.core.entity.culture.CultureEvent;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CultureEventDTO {

    private int id;

    private String genre;

    private String category;

    private String eventName;

    private String place;

    private String price;

    private String url;

    private String thumbnail;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal lat;

    private BigDecimal lon;

    @Builder
    public CultureEventDTO(Integer id, String genre, String category, String eventName, String place,
                           String price, String url, String thumbnail, LocalDate startDate,
                           LocalDate endDate, BigDecimal lat, BigDecimal lon) {
        this.id = id;
        this.genre = genre;
        this.category = category;
        this.eventName = eventName;
        this.place = place;
        this.price = price;
        this.url = url;
        this.thumbnail = thumbnail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lat = lat;
        this.lon = lon;
    }

    public CultureEventDTO(CultureEvent event) {
        this.id = event.getId();
        this.genre = event.getGenre();
        this.category = event.getCategory();
        this.eventName = event.getEventName();
        this.place = event.getPlace();
        this.price = event.getPrice();
        this.url = event.getUrl();
        this.thumbnail = event.getThumbnail();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.lat = event.getLat();
        this.lon = event.getLon();
    }
}
