package hr.fer.pi.geoFighter.service;
import hr.fer.pi.geoFighter.dto.UserCardDTO;
import hr.fer.pi.geoFighter.repository.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@org.springframework.transaction.annotation.Transactional

public class FightService {

    //private final UserCardRepository userCardRepository;
    private final UserRepository userRepository;

    public List<UserCardDTO> getUserCardList(String username) throws MalformedURLException {
        List<UserCardDTO> userCards = new ArrayList<>();
        for(ArrayList<String> card : userRepository.findLocationCards(username)) {
            String name = card.get(0);
            String description = card.get(1);
            URL photoURL = new URL(card.get(2));
            userCards.add(new UserCardDTO(name, description, photoURL));
        }
        return userCards;
    }
}
