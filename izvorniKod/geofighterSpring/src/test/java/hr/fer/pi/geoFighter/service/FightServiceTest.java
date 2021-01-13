package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FightServiceTest {

    @InjectMocks
    private FightService fightService;

    @Test
    void calculateEloScore_winnerHasMore() {
        User winner = new User();
        winner.setEloScore(200);

        User loser = new User();
        loser.setEloScore(200);

        fightService.calculateEloScore(winner, loser);

        assertTrue(winner.getEloScore() > 200);
        assertTrue(loser.getEloScore() < 200);
    }

    @Test
    void calculateEloScore_cantBeNegative() {
        User winner = new User();
        winner.setEloScore(100);

        User loser = new User();
        loser.setEloScore(0);

        fightService.calculateEloScore(winner, loser);

        assertEquals(0, loser.getEloScore());
    }
}
