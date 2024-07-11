package org.duckdns.omaju.core.entity.culture;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.duckdns.omaju.core.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "culture_event")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CultureEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(nullable = false)
    private String genre;

    @NotNull
    @Column(length = 50, nullable = false)
    private String category;

    @NotNull
    @Column(name = "name", nullable = false)
    private String eventName;

    @NotNull
    @Column(nullable = false)
    private String place;

    @NotNull
    @Column(length = 1000, nullable = false)
    private String price;

    @Column(length = 1000)
    private String url;

    @Column(length = 1000)
    private String thumbnail;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal lat;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal lon;

    @Builder
    public CultureEvent(int id, String genre, String category, String eventName, String place, String price, String url,
                        String thumbnail, LocalDate startDate, LocalDate endDate, BigDecimal lat, BigDecimal lon) {
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
}