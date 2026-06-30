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
    private Long projectId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;

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

    public ProjectAssumption(Project project, Integer concurrentUsers, Float headroom, Float requestsPerUserPerSecond, Float apiCallsPerRequest, Float dbRatioPerRequest, Float searchRatioPerRequest, Float cacheRatioPerRequest, Float kafkaRatioPerRequest, Integer logBytesPerRequest, Float authRatio) {
        this.project = project;
        this.concurrentUsers = concurrentUsers;
        this.headroom = headroom;
        this.requestsPerUserPerSecond = requestsPerUserPerSecond;
        this.apiCallsPerRequest = apiCallsPerRequest;
        this.dbRatioPerRequest = dbRatioPerRequest;
        this.searchRatioPerRequest = searchRatioPerRequest;
        this.cacheRatioPerRequest = cacheRatioPerRequest;
        this.kafkaRatioPerRequest = kafkaRatioPerRequest;
        this.logBytesPerRequest = logBytesPerRequest;
        this.authRatio = authRatio;
    }
}
