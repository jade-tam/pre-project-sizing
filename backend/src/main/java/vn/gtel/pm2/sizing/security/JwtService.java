package vn.gtel.pm2.sizing.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import vn.gtel.pm2.sizing.entity.User;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${security.jwt.access-token-expiration}")
    private long accessTokenExpirationSeconds;

    @Value("${security.jwt.refresh-token-expiration}")
    private long refreshTokenExpirationSeconds;

    public String generateAccessToken(User user) {
        return buildToken(user, "access", accessTokenExpirationSeconds);
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, "refresh", refreshTokenExpirationSeconds);
    }

    public String extractUsername(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();
    }

    public boolean isRefreshToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return "refresh".equals(jwt.getClaim("type"));
    }

    private String buildToken(User user, String type, long expirationSeconds) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationSeconds))
                .claim("type", type)
                .claim("email", user.getEmail())
                .claim("username", user.getUsername())
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(),
                        claims
                )
        ).getTokenValue();
    }
}
