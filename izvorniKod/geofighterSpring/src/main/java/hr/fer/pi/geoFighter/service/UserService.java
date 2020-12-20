package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.UserEloDTO;
import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.LocationCard;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

}
