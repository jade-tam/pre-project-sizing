package vn.gtel.pm2.sizing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateProjectInfoRequest {

    @NotBlank
    @Size(min = 3, max = 80)
    private String name;

    @NotBlank
    @Size(min = 5, max = 200)
    private String description;
}
