package vn.gtel.pm2.sizing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogComponentResponse {
    private Long id;
    private String componentKey;
    private String name;
    private String featureName;
    private String machineSpec;
    private Double perMachineCapacity;
    private String capacityUnit;
    private String capacityUnitDescription;
    private Integer haMinimum;
    private String referenceUrl;
    private boolean active;
    private Instant updatedAt;
    private UUID updatedBy;
    private Instant createdAt;
    private UUID createdBy;
}
