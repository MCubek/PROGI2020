package hr.fer.pi.geoFighter.service;

/**
 * @author MatejCubek
 * @project pi
 * @created 01/01/2021
 */
public class LocationService {
    public static double calculateDistance (double lat1, double lat2, double lon1, double lon2){
        final int R = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        distance = Math.pow(distance, 2);
        return Math.sqrt(distance);
    }
}
