package vn.gtel.pm2.sizing.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.gtel.pm2.sizing.constant.AppConstants;

@Data
@NoArgsConstructor
public class AuthRegisterRequest {
    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @NotBlank
    @Size(min = 8, max = 128)
    @Pattern(
            regexp = AppConstants.PASSWORD_REGEX,
            message = "Password must be 8-128 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character."
    )
    private String password;

    @NotBlank
    @Size(min = 3, max = 200)
    private String fullName;

    @NotBlank
    @Email
    @Size(max = 120)
    private String email;
}
