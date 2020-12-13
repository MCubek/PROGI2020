package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.CardDTO;
import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.LocationCard;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.model.UserCard;
import hr.fer.pi.geoFighter.repository.LocationCardRepository;
import hr.fer.pi.geoFighter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class CardService {

    private final LocationCardRepository locationCardRepository;
    private final UserRepository userRepository;

    public List<CardDTO> getCardCollection(String username) {
        List<CardDTO> cardCollection = new ArrayList<>();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new SpringGeoFighterException("User does not exist"));

        if (user.getLocationCardAssoc().isEmpty())
            throw new SpringGeoFighterException("Collection is empty!");

        for (UserCard userCard : user.getLocationCardAssoc()) {

            LocationCard locationCard = userCard.getLocationCard();

            cardCollection.add(new CardDTO(locationCard.getName(), locationCard.getDescription(),
                    locationCard.getPhotoURL(), locationCard.getLocation(),
                    locationCard.getUncommonness(), locationCard.getDifficulty(), locationCard.getPopulation()));
        }

        return cardCollection;
    }

    public CardDTO getLocationCard(long locationId) {

        LocationCard locationCard = locationCardRepository.getLocationCardById(locationId).orElseThrow(() ->
                new SpringGeoFighterException("Card does not exist"));

        CardDTO cardDTO = new CardDTO(locationCard.getName(), locationCard.getDescription(),
                locationCard.getPhotoURL(), locationCard.getLocation(),
                locationCard.getUncommonness(), locationCard.getDifficulty(), locationCard.getPopulation());

        return cardDTO;
    }

    //PRIJAVA KARTE
    public void applyNewLocationCard(String username, String name) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new SpringGeoFighterException("User does not exist"));

        LocationCard locationCard = new LocationCard();
        locationCard.setName(name);
        locationCard.setNeedsToBeChecked(true);
        locationCard.setCreatedBy(user);
    }
}
