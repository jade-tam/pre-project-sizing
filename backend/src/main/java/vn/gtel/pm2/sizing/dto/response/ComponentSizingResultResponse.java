package vn.gtel.pm2.sizing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComponentSizingResultResponse {
    private Long catalogComponentId;
    private String catalogComponentKey;
    private String catalogComponentName;
    private String catalogComponentCapacityUnit;
    private BigDecimal requiredCapacity;
    private Integer requiredMachines;
    private Integer totalMachines;
}
