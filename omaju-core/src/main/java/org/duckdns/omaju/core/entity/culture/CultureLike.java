package org.duckdns.omaju.core.entity.culture;

import jakarta.persistence.*;
import lombok.*;
import org.duckdns.omaju.core.entity.BaseEntity;
import org.duckdns.omaju.core.entity.member.Member;


@Entity
@Table(name = "culture_like")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CultureLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "culture_event_id", nullable = false)
    private CultureEvent cultureEvent;

    @Builder
    public CultureLike(Member member, CultureEvent cultureEvent) {
        this.member = member;
        this.cultureEvent = cultureEvent;
    }
}