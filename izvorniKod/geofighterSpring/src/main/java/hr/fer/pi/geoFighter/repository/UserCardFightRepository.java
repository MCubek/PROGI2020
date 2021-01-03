package hr.fer.pi.geoFighter.repository;

import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.model.UserCardFight;
import hr.fer.pi.geoFighter.model.UserCardFightId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UserCardFightRepository extends JpaRepository<UserCardFight, UserCardFightId> {

    Collection<UserCardFight> findUserCardFightByUser(User user);
}
