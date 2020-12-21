package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.UserEloDTO;
import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class UserService {

    private final UserRepository userRepository;

    public List<UserEloDTO> getUserEloInfo() {
        return new ArrayList<>(userRepository.getUserEloInfo());
    }

    public List<String> getNearbyUsers(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringGeoFighterException("User does not exist"));
        Point2D.Double userLocation = user.getCurrentLocation();
        double userLongitude = userLocation.getX();
        double userLatitude = userLocation.getY();

        Collection<User> allUsers = userRepository.findUsersByCurrentLocationIsNotNull();
        ArrayList<String> nearbyUsers = new ArrayList<>();
        for (User u : allUsers) {
            Point2D.Double location = u.getCurrentLocation();
            double uLongitude = location.getX();
            double uLatitude = location.getY();
            double distance = calculateDistance(userLatitude, uLatitude, userLongitude, uLongitude);
            if (distance <= 50.0 && (!user.getUsername().equals(u.getUsername()))) {
                nearbyUsers.add(u.getUsername());
            }
        }
        return nearbyUsers;
    }

    public double calculateDistance (double lat1, double lat2, double lon1, double lon2){
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
