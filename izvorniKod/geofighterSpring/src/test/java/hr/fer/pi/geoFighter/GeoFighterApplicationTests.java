package hr.fer.pi.geoFighter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeoFighterApplicationTests {

    @Test
    void loadsGeo() {
        Assertions.assertEquals(10, 5 + 5);
    }
}
