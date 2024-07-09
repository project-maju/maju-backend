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

    private Integer id;

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
    public CultureEventDTO(CultureEvent cultureEvent) {
        this.id = cultureEvent.getId();
        this.category = cultureEvent.getCategory();
        this.eventName = cultureEvent.getEventName();
        this.place = cultureEvent.getPlace();
        this.price = cultureEvent.getPrice();
        this.url = cultureEvent.getPrice();
        this.thumbnail = cultureEvent.getThumbnail();
        this.startDate = cultureEvent.getStartDate();
        this.endDate = cultureEvent.getEndDate();
        this.lat = cultureEvent.getLat();
        this.lon = cultureEvent.getLon();
    }
}
