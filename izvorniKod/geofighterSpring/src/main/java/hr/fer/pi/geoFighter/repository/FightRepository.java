package hr.fer.pi.geoFighter.repository;

import hr.fer.pi.geoFighter.model.Fight;
import hr.fer.pi.geoFighter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FightRepository extends JpaRepository<Fight, Long> {
    List<Fight> getAllByWinner(User winner);
}
