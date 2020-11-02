package hr.fer.pi.geoFighter.repository;

import hr.fer.pi.geoFighter.model.Role;
import hr.fer.pi.geoFighter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> getRoleByName(String name);
}
