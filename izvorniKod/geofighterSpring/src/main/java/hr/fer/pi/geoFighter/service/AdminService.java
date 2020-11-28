package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.CartographerStatus;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class AdminService {

    private final UserRepository userRepository;

    public List<String> getCartographerApplies() {
        List<String> usernames = new ArrayList<>();
        for(User user : userRepository.findUsersByCartographerStatus(CartographerStatus.APPLIED))
            usernames.add(user.getUsername());

        return usernames;
    }

    public void acceptCartographerApply(String username) {
        System.out.println(username);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new SpringGeoFighterException("User does not exist"));

        user.setCartographerStatus(CartographerStatus.APPROVED);
    }
}
