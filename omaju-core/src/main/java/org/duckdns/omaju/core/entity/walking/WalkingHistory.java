package org.duckdns.omaju.core.entity.walking;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.duckdns.omaju.core.entity.BaseEntity;
import org.duckdns.omaju.core.entity.member.Member;

@Entity
@Table(name = "WALKING_HISTORY")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class WalkingHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    private double distance;

    private int steps;

    @Builder
    public WalkingHistory(int id, Member member, double distance, int steps) {
        this.id = id;
        this.member = member;
        this.distance = distance;
        this.steps = steps;
    }
}
