package hr.fer.pi.geoFighter.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author MatejCubek
 * @project pi
 * @created 11/01/2021
 */

class LocationServiceTest {

    @Test
    public void calculateDistanceTest() {
        var latitude1 = 45.25;
        var longitude1 = 15.25;
        var latitude2 = 46.0;
        var longitude2 = 16.75;

        var value = LocationService.calculateDistance(latitude1, latitude2, longitude1, longitude2);

        Assertions.assertTrue(Math.abs(value - 143) <= 1);
    }
}
