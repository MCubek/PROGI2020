package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.CardApplicationDTO;
import hr.fer.pi.geoFighter.dto.CardLocationDTO;
import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.LocationCard;
import hr.fer.pi.geoFighter.repository.LocationCardRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class CardApplicationService {

    private final LocationCardRepository locationCardRepository;
    private final AuthService authService;

    public List<CardApplicationDTO> getAllCardApplications() {
        List<CardApplicationDTO> cardCollection = new ArrayList<>();

        for (LocationCard locationCard : locationCardRepository.getLocationCardsByAccepted(false)) {
            CardApplicationDTO card = new CardApplicationDTO();
            card.setId(locationCard.getId());
            card.setName(locationCard.getName());
            card.setDescription(locationCard.getDescription());
            card.setPhotoURL(locationCard.getPhotoURL());
            card.setLocation(CardService.getLocationString(locationCard.getLocation()));
            card.setCreatedBy(locationCard.getCreatedBy() != null ? "locationCard.getCreatedBy().getUsername()" : "unknown");
            card.setNeedsToBeChecked(locationCard.isNeedsToBeChecked());
            card.setUncommonness(locationCard.getUncommonness());
            card.setDifficulty(locationCard.getDifficulty());
            card.setPopulation(locationCard.getPopulation());
            card.setCreatedTime(CardService.getTime(locationCard.getCreatedDate()));
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
        card.setNeedsToBeChecked(false);
    }

    public void declineCardApplication(Long id) {
        LocationCard card = locationCardRepository.getLocationCardById(id).orElseThrow(
                () -> new SpringGeoFighterException("Card does not exist"));

        locationCardRepository.delete(card);
    }

    public void editCardApplication(CardApplicationDTO card) {
        LocationCard locationCard = locationCardRepository.getLocationCardById(card.getId()).orElseThrow(
                () -> new SpringGeoFighterException("Card does not exist"));

        locationCard.setName(card.getName());
        locationCard.setDescription(card.getDescription());
        locationCard.setPhotoURL(card.getPhotoURL());
        locationCard.setLocation(CardService.parseLocationString(card.getLocation()));
        locationCard.setUncommonness(card.getUncommonness());
        locationCard.setDifficulty(card.getDifficulty());
        locationCard.setPopulation(card.getPopulation());

    }

    public void requestCardApplicationConfirmation(Long id) {
        LocationCard card = locationCardRepository.getLocationCardById(id).orElseThrow(
                () -> new SpringGeoFighterException("Card does not exist"));

        card.setNeedsToBeChecked(! card.isNeedsToBeChecked());
    }

    public List<CardLocationDTO> getAllCardsThatNeedToBeChecked() {
        return locationCardRepository.getLocationCardByNeedsToBeCheckedIsTrue().stream()
                .filter(v->v.getLocation()!=null)
                .map(v -> new CardLocationDTO(v.getId(), v.getLocation().getX(), v.getLocation().getY()))
                .collect(Collectors.toList());
    }
}
