package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.CardDTO;
import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.exceptions.UserInfoInvalidException;
import hr.fer.pi.geoFighter.model.LocationCard;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.model.UserCard;
import hr.fer.pi.geoFighter.repository.LocationCardRepository;
import hr.fer.pi.geoFighter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class CardService {

    private final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
    private final LocationCardRepository locationCardRepository;
    private final UserRepository userRepository;
    private final AuthService authService;


    public List<CardDTO> getAllCards() {
        List<CardDTO> cardCollection = new ArrayList<>();

        for (LocationCard locationCard : locationCardRepository.findAll()) {
            if (locationCard.isNeedsToBeChecked() == false) {

                CardDTO cardDTO = new CardDTO();

                cardDTO.setId(locationCard.getId());
                cardDTO.setName(locationCard.getName());
                cardDTO.setDescription(locationCard.getDescription());
                cardDTO.setPhotoUrl(locationCard.getPhotoURL());
                cardDTO.setUncommonness(locationCard.getUncommonness());
                cardDTO.setDifficulty(locationCard.getDifficulty());
                cardDTO.setPopulation(locationCard.getPopulation());
                cardCollection.add(cardDTO);
            }
        }
        return cardCollection;
    }

    public CardDTO getLocationCard(long id) {

        LocationCard locationCard = locationCardRepository.getLocationCardById(id).orElseThrow(() ->
                new SpringGeoFighterException("Card does not exist"));

        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(id);
        cardDTO.setName(locationCard.getName());
        cardDTO.setDescription(locationCard.getDescription());
        cardDTO.setPhotoUrl(locationCard.getPhotoURL());
        cardDTO.setLocation(getLocationString(locationCard.getLocation()));
        cardDTO.setCreatedBy(locationCard.getCreatedBy().getUsername());

        return cardDTO;
    }

    //PRIJAVA KARTE

    public void applyLocationCard(CardDTO cardDTO) {

        LocationCard locationCard = new LocationCard();
        locationCard.setName(cardDTO.getName());
        locationCard.setDescription(cardDTO.getDescription());
        locationCard.setPhotoURL(cardDTO.getPhotoUrl());
        locationCard.setLocation(parseLocationString(cardDTO.getLocation()));
        locationCard.setNeedsToBeChecked(true);
        locationCard.setCreatedBy(authService.getCurrentUser());

        if (!urlValidator.isValid(cardDTO.getPhotoUrl().toString()))
            throw new UserInfoInvalidException("Invalid photo URL");

        try {
            locationCard.setPhotoURL(new URL(cardDTO.getPhotoUrl().toString()));
        } catch (MalformedURLException exception) {
            throw new SpringGeoFighterException("Image URL error");
        }
        
        locationCardRepository.save(locationCard);
    }

    public List<CardDTO> getCardCollection() {
        return locationCardRepository.findAll().stream()
                .map(lc -> CardDTO.builder()
                        .id(lc.getId())
                        .name(lc.getName())
                        .description(lc.getDescription())
                        .photoUrl(lc.getPhotoURL())
                        .location(getLocationString(lc.getLocation()))
                        .createdBy(lc.getCreatedBy().getUsername())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteLocationCard(long locationCardId) {
        locationCardRepository.deleteById(locationCardId);
    }

    private static String getLocationString(Point2D.Double point) {
        return point.getX() + ", " + point.getY();
    }

    private static Point2D.Double parseLocationString(String locationString) {
        var array = locationString.split("\\b+");

        if (array.length != 2) throw new IllegalArgumentException("2 coordinates missing!");

        return new Point2D.Double(Double.parseDouble(array[0]), Double.parseDouble(array[1]));
    }
}
