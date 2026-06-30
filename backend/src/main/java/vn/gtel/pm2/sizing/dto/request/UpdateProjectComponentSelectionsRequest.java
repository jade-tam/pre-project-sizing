package vn.gtel.pm2.sizing.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UpdateProjectComponentSelectionsRequest {

    private List<Long> selectedCatalogComponentIds;
}
