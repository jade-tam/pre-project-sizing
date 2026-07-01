package vn.gtel.pm2.sizing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private UserResponse owner;
    private String name;
    private String description;
    private List<CatalogComponentResponse> selectedCatalogComponents;
    private ProjectAssumptionResponse projectAssumption;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
}
