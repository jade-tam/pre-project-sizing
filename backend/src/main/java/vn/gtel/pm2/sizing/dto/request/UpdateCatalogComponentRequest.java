package vn.gtel.pm2.sizing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
public class UpdateCatalogComponentRequest {

    @NotBlank
    @Size(min = 3, max = 100)
    private String featureName;

    @NotBlank
    @Size(min = 3, max = 100)
    private String machineSpec;

    @NotNull
    @Positive
    private Double perMachineCapacity;

    @NotBlank
    @Size(min = 1, max = 50)
    private String capacityUnit;

    @NotBlank
    @Size(min = 5, max = 200)
    private String capacityUnitDescription;

    @NotNull
    @Positive
    private Integer haMinimum;

    @URL
    @Size(min = 5, max = 240)
    private String referenceUrl;

    @NotNull
    private boolean isActive;
}
