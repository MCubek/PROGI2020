package hr.fer.pi.geoFighter.repository;

import hr.fer.pi.geoFighter.model.Fight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FightRepository extends JpaRepository<Fight, Long> {
}
