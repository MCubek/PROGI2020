package hr.fer.pi.geoFighter.service;

import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author MatejCubek
 * @project pi
 * @created 11/01/2021
 */
class CardServiceTest {

    @Test
    void getLocationString() {
        var point = new Point2D.Double(11.11, 12.12);
        assertEquals("11.11, 12.12", CardService.getLocationString(point));
    }

    @Test
    void parseLocationString() {
        var locationString = "11.11 12.12";

        assertEquals(new Point2D.Double(11.11, 12.12), CardService.parseLocationString(locationString));
    }

    @Test
    void getTime() {
        Instant time = Instant.EPOCH;

        assertEquals("1970-01-01 01:00", CardService.getTime(time));
    }
}