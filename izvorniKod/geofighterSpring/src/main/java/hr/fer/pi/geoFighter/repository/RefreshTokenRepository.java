package hr.fer.pi.geoFighter.repository;

import hr.fer.pi.geoFighter.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author MatejCubek
 * @project pi
 * @created 07/11/2020
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);
}
