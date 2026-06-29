package vn.gtel.pm2.sizing.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String type;
    private long expiresIn;
}
