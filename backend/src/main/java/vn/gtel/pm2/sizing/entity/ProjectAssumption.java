package vn.gtel.pm2.sizing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class ProjectAssumption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private Integer concurrentUsers;

    @Setter
    @Column(nullable = false)
    private Float headroom;

    @Setter
    @Column(nullable = false)
    private Float requestsPerUserPerSecond;

    @Setter
    @Column(nullable = false)
    private Float apiCallsPerRequest;

    @Setter
    @Column(nullable = false)
    private Float dbRatioPerRequest;

    @Setter
    @Column(nullable = false)
    private Float searchRatioPerRequest;

    @Setter
    @Column(nullable = false)
    private Float cacheRatioPerRequest;

    @Setter
    @Column(nullable = false)
    private Float kafkaRatioPerRequest;

    @Setter
    @Column(nullable = false)
    private Integer logBytesPerRequest;

    @Setter
    @Column(nullable = false)
    private Float authRatio;
}
