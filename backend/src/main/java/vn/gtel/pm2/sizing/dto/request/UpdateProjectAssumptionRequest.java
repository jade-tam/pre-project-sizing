package vn.gtel.pm2.sizing.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateProjectAssumptionRequest {

    @Positive
    @NotNull
    private Integer concurrentUsers;

    @Positive
    @NotNull
    private Float headroom;

    @Positive
    @NotNull
    private Float requestsPerUserPerSecond;

    @Positive
    @NotNull
    private Float apiCallsPerRequest;

    @Positive
    @NotNull
    private Float dbRatioPerRequest;

    @Positive
    @NotNull
    private Float searchRatioPerRequest;

    @Positive
    @NotNull
    private Float cacheRatioPerRequest;

    @Positive
    @NotNull
    private Float kafkaRatioPerRequest;

    @Positive
    @NotNull
    private Integer logBytesPerRequest;

    @Positive
    @NotNull
    private Float authRatio;
}
