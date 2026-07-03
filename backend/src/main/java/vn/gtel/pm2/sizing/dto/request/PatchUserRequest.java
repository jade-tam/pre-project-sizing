package vn.gtel.pm2.sizing.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
public class PatchUserRequest {

    @NotBlank
    @Size(min = 3, max = 200)
    private String fullName;

    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @URL
    @Size(min = 3, max = 100)
    private String avatarUrl;

    @Size(min = 3, max = 100)
    private String displayName;

    @Size(min = 1, max = 100)
    private String pronouns;

    @Size(min = 3, max = 500)
    private String bio;
}
