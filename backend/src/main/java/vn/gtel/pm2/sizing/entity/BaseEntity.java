package vn.gtel.pm2.sizing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Getter
public abstract class BaseEntity {
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Setter
    @Column(name = "deleted_at")
    private Instant deletedAt;

    @Setter
    @Column(name = "created_by")
    private UUID createdBy;

    @Setter
    @Column(name = "updated_by")
    private UUID updatedBy;

    @Setter
    @Column(name = "deleted_by")
    private UUID deletedBy;

    @Setter
    @Column(name = "deleted")
    private boolean deleted = false;
}
