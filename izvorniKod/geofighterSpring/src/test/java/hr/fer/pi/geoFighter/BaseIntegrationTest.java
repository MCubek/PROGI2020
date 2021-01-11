package hr.fer.pi.geoFighter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

/**
 * @author MatejCubek
 * @project pi
 * @created 11/01/2021
 */

@SpringBootTest
public class BaseIntegrationTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeAll
    static void test() {
        Assertions.assertDoesNotThrow(() -> {
        });
    }
}
