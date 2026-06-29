package vn.gtel.pm2.sizing.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthRefreshRequest {
    @NotBlank
    private String refreshToken;
}
