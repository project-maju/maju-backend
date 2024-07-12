package org.duckdns.omaju.core.entity.walking;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "WALKING_TRAILS")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class WalkingTrails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(length = 100)
    private String name;

    @NotNull
    @Column(length = 10)
    private String level;

    @NotNull
    private double startLat;

    @NotNull
    private double startLon;

    @NotNull
    private double endLat;

    @NotNull
    private double endLon;

    @Builder
    public WalkingTrails(int id, String name, String level, double startLat, double startLon, double endLat, double endLon) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;
    }
}
