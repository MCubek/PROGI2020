package hr.fer.pi.geoFighter.repository;

import hr.fer.pi.geoFighter.model.UserCard;
import hr.fer.pi.geoFighter.model.UserCardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, UserCardId> {
}
