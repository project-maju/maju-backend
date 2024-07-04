package org.duckdns.omaju.core.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.Instant;

public class AuditingEntityListener {
    @PrePersist
    public void prePersist(Object object) {
        if (object instanceof BaseEntity) {
            BaseEntity entity = (BaseEntity) object;
            entity.setCreatedAt(Instant.now().getEpochSecond());
            entity.setUpdatedAt(Instant.now().getEpochSecond());
        }
    }

    @PreUpdate
    public void preUpdate(Object object) {
        if (object instanceof BaseEntity) {
            BaseEntity entity = (BaseEntity) object;
            entity.setUpdatedAt(Instant.now().getEpochSecond());
        }
    }
}
