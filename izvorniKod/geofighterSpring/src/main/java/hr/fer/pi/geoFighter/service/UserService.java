package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.SendRequestDTO;
import hr.fer.pi.geoFighter.dto.UserEloDTO;
import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.dto.UserLocationDTO;
import hr.fer.pi.geoFighter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final List<SendRequestDTO> requests;
    private final List<SendRequestDTO> startPlaying;

    public List<UserEloDTO> getUserEloInfo() {
        return new ArrayList<>(userRepository.getUserEloInfo());
    }

    public List<String> getEnabledUsers() {
        return new ArrayList<>(userRepository.findEnabledUsernames());
    }

    public List<String> userProfile(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new SpringGeoFighterException("User does not exist"));
        int wins = userRepository.findStatisticWins(username);
        int losses = userRepository.findStatisticLoss(username);
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

    public void storeLocation(UserLocationDTO userLocationDTO){
        User user = authService.getCurrentUser();
        Point2D.Double point = new Point2D.Double(userLocationDTO.getLatitude(),userLocationDTO.getLongitude());
        user.setCurrentLocation(point);
        userRepository.save(user);
    }

    public void sendRequest(SendRequestDTO sendRequestDTO){
        requests.add(sendRequestDTO);
    }

    public List<String> getRequests(String username){
        List<String> yourRequests = new ArrayList<>();
        for (SendRequestDTO u:requests) {
            if(u.getUsernameReceiver().equals(username)){
                yourRequests.add(u.getUsernameSender());
            }
        }
        return yourRequests;
    }

    public void processAnswer(SendRequestDTO answer){
        List<SendRequestDTO> copy = List.copyOf(requests);
        if(answer.isAnswer()){
            startPlaying.add(answer);
            for (SendRequestDTO request: copy) {
                if(request.getUsernameSender().equals(answer.getUsernameSender())){
                    requests.remove(request);
                }
            }
        }
        else{
            requests.remove(answer);
        }
    }

    public String getMatches(String username){
        String partner = "";
        for (SendRequestDTO matches:startPlaying){
            if (matches.getUsernameSender().equals(username)){
               partner = matches.getUsernameReceiver();
            }
            else if(matches.getUsernameReceiver().equals(username)){
               partner = matches.getUsernameSender();
            }
        }
        return partner;
    }
}
