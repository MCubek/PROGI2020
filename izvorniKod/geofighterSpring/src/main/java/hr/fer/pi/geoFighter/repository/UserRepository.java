package hr.fer.pi.geoFighter.repository;

import hr.fer.pi.geoFighter.model.CartographerStatus;
import hr.fer.pi.geoFighter.model.Role;
import hr.fer.pi.geoFighter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Collection<User> findUsersByRole(Role role);

    Collection<User> findUsersByCurrentLocationIsNotNull();

    Collection<User> findUsersByCartographerStatus(CartographerStatus status);
}
