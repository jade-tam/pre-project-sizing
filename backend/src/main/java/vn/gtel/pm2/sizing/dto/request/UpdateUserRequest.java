package vn.gtel.pm2.sizing.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserRequest {

    @NotBlank
    @Size(min = 3, max = 200)
    private String fullName;

    @Email
    @Size(max = 120)
    private String email;
}
