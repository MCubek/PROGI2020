package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.UserDTO;
import hr.fer.pi.geoFighter.dto.UserEloDTO;
import hr.fer.pi.geoFighter.dto.UserLocationDTO;
import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.Fight;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.model.UserCardFight;
import hr.fer.pi.geoFighter.repository.UserCardFightRepository;
import hr.fer.pi.geoFighter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserCardFightRepository userCardFightRepository;
    private final AuthService authService;

    public List<UserEloDTO> getUserEloInfo() {
        return userRepository.findUsersByEnabledTrueOrderByEloScoreDesc()
                .stream()
                .map(user -> {
                    UserEloDTO userEloDTO = new UserEloDTO();
                    userEloDTO.setUsername(user.getUsername());
                    userEloDTO.setWins(user.getWins());
                    userEloDTO.setLosses(user.getLosses());
                    userEloDTO.setEloScore(user.getEloScore());
                    return userEloDTO;
                })
                .collect(Collectors.toList());
    }

    public List<String> getEnabledUsers() {
        return userRepository.findUsersByEnabledTrueOrderByUsernameAsc().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    private long getWinsInLast10FightsForUser(User user) {
        return userCardFightRepository.findUserCardFightByUser(user)
                .stream()
                .map(UserCardFight::getFight)
                .sorted(Comparator.comparing(Fight::getStartTime).reversed())
                .limit(10)
                .map(Fight::getWinner)
                .filter(w -> w.getUserId().equals(user.getUserId()))
                .count();
    }

    private long getLosesInLast10FightsForUser(User user) {
        return userCardFightRepository.findUserCardFightByUser(user)
                .stream()
                .map(UserCardFight::getFight)
                .sorted(Comparator.comparing(Fight::getStartTime).reversed())
                .limit(10)
                .map(Fight::getWinner)
                .filter(w -> !w.getUserId().equals(user.getUserId()))
                .count();
    }

    public List<String> userProfile(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringGeoFighterException("User does not exist"));
        long wins = getWinsInLast10FightsForUser(user);
        long losses = getLosesInLast10FightsForUser(user);
        ArrayList<String> data = new ArrayList<>();
        data.add(user.getUsername());
        data.add(String.valueOf(user.getEloScore()));
        data.add(String.valueOf(wins));
        data.add(String.valueOf(losses));
        if(user.getPhotoURL()==null){
            data.add("https://cdn0.iconfinder.com/data/icons/set-ui-app-android/32/8-512.png");
            // source: https://www.iconfinder.com/icons/3898372/_icon
        }else{
            data.add(user.getPhotoURL().toString());
        }
        return data;
    }


    public List<String> getNearbyUsers(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringGeoFighterException("User does not exist"));
        Point2D.Double userLocation = user.getCurrentLocation();

        if (userLocation == null) userLocation = new Point2D.Double(0, 0);


        double userLongitude = userLocation.getX();
        double userLatitude = userLocation.getY();

        Collection<User> allUsers = userRepository.findUsersByCurrentLocationIsNotNull();
        ArrayList<String> nearbyUsers = new ArrayList<>();
        for (User u : allUsers) {
            Point2D.Double location = u.getCurrentLocation();

            if (location == null) location = new Point2D.Double(0, 0);

            double uLongitude = location.getX();
            double uLatitude = location.getY();
            double distance = LocationService.calculateDistance(userLatitude, uLatitude, userLongitude, uLongitude);
            if (distance <= 50.0 && (!user.getUsername().equals(u.getUsername()))) {
                nearbyUsers.add(u.getUsername());
            }
        }
        return nearbyUsers;
    }

    public void storeLocation(UserLocationDTO userLocationDTO){
        User user = authService.getCurrentUser();
        Point2D.Double point = new Point2D.Double(userLocationDTO.getLatitude(),userLocationDTO.getLongitude());
        user.setCurrentLocation(point);
        userRepository.save(user);
    }

    public void removeLocation(){
        User user = authService.getCurrentUser();
        user.setCurrentLocation(null);
        userRepository.save(user);
    }

    public static UserDTO createUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhotoURL(user.getPhotoURL());
        userDTO.setRole(user.getRole().getName());

        return userDTO;
    }
}
