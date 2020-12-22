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

    public List<CardDTO> getCardCollection(String username) {
        List<CardDTO> cardCollection = new ArrayList<>();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new SpringGeoFighterException("User does not exist"));

        if (user.getLocationCardAssoc().isEmpty())
            throw new SpringGeoFighterException("Collection is empty!");

        for (UserCard userCard : user.getLocationCardAssoc()) {

            LocationCard locationCard = userCard.getLocationCard();

            CardDTO cardDTO = new CardDTO();
            cardDTO.setId(locationCard.getId());
            cardDTO.setName(locationCard.getName());
            cardDTO.setDescription(locationCard.getDescription());
            cardDTO.setPhotoUrl(locationCard.getPhotoURL());
            cardDTO.setLocation(getLocationString(locationCard.getLocation()));
            cardDTO.setCreatedBy(locationCard.getCreatedBy().getUsername());

            cardCollection.add(cardDTO);
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
        if (locationCardRepository.getLocationCardById(cardDTO.getId()).isPresent()) {
            throw new SpringGeoFighterException("Card already exists!");
        }

        LocationCard locationCard = new LocationCard();
        locationCard.setName(cardDTO.getName());
        locationCard.setId(cardDTO.getId());
        locationCard.setDescription(cardDTO.getDescription());
        locationCard.setPhotoURL(cardDTO.getPhotoUrl());
        locationCard.setLocation(parseLocationString(cardDTO.getLocation()));
        locationCard.setNeedsToBeChecked(true);
        locationCard.setCreatedBy(authService.getCurrentUser());

        if (! urlValidator.isValid(cardDTO.getPhotoUrl().toString()))
            throw new UserInfoInvalidException("Invalid photo URL");

        try {
            locationCard.setPhotoURL(new URL(cardDTO.getPhotoUrl().toString()));
        } catch (MalformedURLException exception) {
            throw new SpringGeoFighterException("Image URL error");
        }

        //?
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
