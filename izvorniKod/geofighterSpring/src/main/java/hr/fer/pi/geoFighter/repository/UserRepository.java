package hr.fer.pi.geoFighter.repository;

import hr.fer.pi.geoFighter.model.CartographerStatus;
import hr.fer.pi.geoFighter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Collection<User> findUsersByCurrentLocationIsNotNull();

    Collection<User> findUsersByCartographerStatus(CartographerStatus status);

    Collection<User> findUsersByEnabledTrueOrderByEloScoreDesc();

    Collection<User> findUsersByEnabledTrue();

    @Query(value="select count(user_id) from (select winner_user_id, users.user_id from user_card_fight join fights on user_card_fight.fight_id=fights.fight_id join users on \n" +
            "users.user_id=user_card_fight.user_id where username=?1\n" +
            "order by start_time desc limit 10) as lastTen where winner_user_id=user_id;", nativeQuery=true)
    int findStatisticWins(String username);

    @Query(value = "select count(user_id) from (select winner_user_id, users.user_id from user_card_fight join fights on user_card_fight.fight_id=fights.fight_id join users on \n" +
            "users.user_id=user_card_fight.user_id where username=?1\n" +
            "order by start_time desc limit 10) as lastTen where winner_user_id!=user_id;", nativeQuery = true)
    int findStatisticLoss(String username);

}
