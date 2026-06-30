package vn.gtel.pm2.sizing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectAssumptionResponse {
    private Integer concurrentUsers;
    private Float headroom;
    private Float requestsPerUserPerSecond;
    private Float apiCallsPerRequest;
    private Float dbRatioPerRequest;
    private Float searchRatioPerRequest;
    private Float cacheRatioPerRequest;
    private Float kafkaRatioPerRequest;
    private Integer logBytesPerRequest;
    private Float authRatio;
}
