package hr.fer.pi.geoFighter.repository;

import hr.fer.pi.geoFighter.model.Privilege;
import hr.fer.pi.geoFighter.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    Optional<Role> findRoleByPrivilegesContains(Privilege privilege);
}
