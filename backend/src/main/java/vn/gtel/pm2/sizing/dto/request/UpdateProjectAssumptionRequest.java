package vn.gtel.pm2.sizing.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateProjectAssumptionRequest {

    @PositiveOrZero
    @NotNull
    private Integer concurrentUsers;

    @PositiveOrZero
    @NotNull
    private Float headroom;

    @PositiveOrZero
    @NotNull
    private Float requestsPerUserPerSecond;

    @PositiveOrZero
    @NotNull
    private Float apiCallsPerRequest;

    @PositiveOrZero
    @NotNull
    private Float dbRatioPerRequest;

    @PositiveOrZero
    @NotNull
    private Float searchRatioPerRequest;

    @PositiveOrZero
    @NotNull
    private Float cacheRatioPerRequest;

    @PositiveOrZero
    @NotNull
    private Float kafkaRatioPerRequest;

    @PositiveOrZero
    @NotNull
    private Integer logBytesPerRequest;

    @PositiveOrZero
    @NotNull
    private Float authRatio;
}
