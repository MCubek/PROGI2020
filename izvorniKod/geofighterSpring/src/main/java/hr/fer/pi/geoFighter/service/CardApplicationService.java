package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.LocationCard;
import hr.fer.pi.geoFighter.repository.LocationCardRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class CardApplicationService {

    private final LocationCardRepository locationCardRepository;
    private final AuthService authService;

    public List<LocationCard> getAllCardApplications() {
        List<LocationCard> cardCollection = new ArrayList<>();

        for (LocationCard locationCard : locationCardRepository.getLocationCardsByAccepted(false)) {
            LocationCard card = new LocationCard();
            card.setId(locationCard.getId());
            card.setName(locationCard.getName());
            card.setDescription(locationCard.getDescription());
            card.setPhotoURL(locationCard.getPhotoURL());
            card.setLocation(locationCard.getLocation());
            card.setCreatedBy(locationCard.getCreatedBy());
            cardCollection.add(card);
        }

        return cardCollection;
    }

    public void acceptCardApplication(Long id) {
        LocationCard card = locationCardRepository.getLocationCardById(id).orElseThrow(
                () -> new SpringGeoFighterException("Card does not exist"));

        card.setAccepted(true);
        card.setEnabledDate(Instant.now());
        card.setAcceptedBy(authService.getCurrentUser());
    }

    public void declineCardApplication(Long id) {
        LocationCard card = locationCardRepository.getLocationCardById(id).orElseThrow(
                () -> new SpringGeoFighterException("Card does not exist"));

        locationCardRepository.delete(card);
    }

    public void editCardApplication(LocationCard card) {
        LocationCard oldCard = locationCardRepository.getLocationCardById(card.getId()).orElseThrow(
                () -> new SpringGeoFighterException("Card does not exist"));

        locationCardRepository.delete(oldCard);
        locationCardRepository.save(card);
    }

    public void requestCardApplicationConfirmation(Long id) {
        LocationCard card = locationCardRepository.getLocationCardById(id).orElseThrow(
                () -> new SpringGeoFighterException("Card does not exist"));

        card.setNeedsToBeChecked(true);
    }
}
