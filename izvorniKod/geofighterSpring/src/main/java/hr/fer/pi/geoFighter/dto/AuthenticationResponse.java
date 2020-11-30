package hr.fer.pi.geoFighter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String authorizationToken;
    private String refreshToken;
    private Instant expiresAt;
    private String username;
    private String role;
}
