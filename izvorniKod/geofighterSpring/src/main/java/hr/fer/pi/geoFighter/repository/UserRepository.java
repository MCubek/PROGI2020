package hr.fer.pi.geoFighter.repository;

import hr.fer.pi.geoFighter.dto.UserEloDTO;
import hr.fer.pi.geoFighter.model.CartographerStatus;
import hr.fer.pi.geoFighter.model.Role;
import hr.fer.pi.geoFighter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Collection<User> findUsersByRole(Role role);

    Collection<User> findUsersByCurrentLocationIsNotNull();

    Collection<User> findUsersByCartographerStatus(CartographerStatus status);

    @Query(value = "select  location_cards.name, location_cards.description, location_cards.photourl\n" +
            "from users join user_card on users.user_id=user_card.user_id join location_cards on user_card.lcd_id=location_cards.lcd_id\n" +
            "where users.username=?1\t", nativeQuery = true)
    List<ArrayList<String>> findLocationCards(String username);

    @Query("SELECT new hr.fer.pi.geoFighter.dto.UserEloDTO(u.username, u.wins, u.losses, u.eloScore) FROM hr.fer.pi.geoFighter.model.User u WHERE u.enabled = true ORDER BY u.eloScore DESC")
    Collection<UserEloDTO> getUserEloInfo();

    @Query("SELECT u.username FROM hr.fer.pi.geoFighter.model.User u WHERE u.enabled = true ORDER BY u.username")
    Collection<String> findEnabledUsernames();

    @Query(value="select count(user_id) from (select winner_user_id, users.user_id from user_card_fight join fights on user_card_fight.fight_id=fights.fight_id join users on \n" +
            "users.user_id=user_card_fight.user_id where username=?1\n" +
            "order by start_time desc limit 10) as lastTen where winner_user_id=user_id;", nativeQuery=true)
    int findStatisticWins(String username);

    @Query(value = "select count(user_id) from (select winner_user_id, users.user_id from user_card_fight join fights on user_card_fight.fight_id=fights.fight_id join users on \n" +
            "users.user_id=user_card_fight.user_id where username=?1\n" +
            "order by start_time desc limit 10) as lastTen where winner_user_id!=user_id;", nativeQuery = true)
    int findStatisticLoss(String username);

}
