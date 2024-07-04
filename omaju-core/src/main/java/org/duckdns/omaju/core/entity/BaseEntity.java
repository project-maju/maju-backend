package org.duckdns.omaju.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public class BaseEntity {
    @Column(updatable = false)
    @CreatedDate
    private Long createdAt;

    @LastModifiedDate
    private Long updatedAt;
}
