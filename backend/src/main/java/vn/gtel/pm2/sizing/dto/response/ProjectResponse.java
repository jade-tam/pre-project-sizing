package vn.gtel.pm2.sizing.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
}
