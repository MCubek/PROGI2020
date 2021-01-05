package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.CardDTO;
import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.exceptions.UserInfoInvalidException;
import hr.fer.pi.geoFighter.model.LocationCard;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.model.UserCard;
import hr.fer.pi.geoFighter.model.UserCardId;
import hr.fer.pi.geoFighter.repository.LocationCardRepository;
import hr.fer.pi.geoFighter.repository.UserCardRepository;
import hr.fer.pi.geoFighter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class CardService {

    private final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
    private final LocationCardRepository locationCardRepository;
    private final UserRepository userRepository;
    private final UserCardRepository userCardRepository;
    private final AuthService authService;

    private static final int MAX_CARD_DISTANCE = 3;


    public List<CardDTO> getAllCards() {
        return locationCardRepository.getLocationCardByAcceptedIsTrue().stream()
                .map(CardService::createCardDTO)
                .collect(Collectors.toList());

    }

    public CardDTO getLocationCard(long id) {

        LocationCard locationCard = locationCardRepository.getLocationCardById(id).orElseThrow(() ->
                new SpringGeoFighterException("Card does not exist"));

        return createCardDTO(locationCard);
    }

    //PRIJAVA KARTE

    public void applyLocationCard(CardDTO cardDTO) {

        LocationCard locationCard = new LocationCard();
        locationCard.setName(cardDTO.getName());
        locationCard.setDescription(cardDTO.getDescription());
        locationCard.setPhotoURL(cardDTO.getPhotoUrl());
        locationCard.setLocation(parseLocationString(cardDTO.getLocation()));
        locationCard.setCreatedBy(authService.getCurrentUser());
        locationCard.setDifficulty(cardDTO.getDifficulty());
        locationCard.setPopulation(cardDTO.getPopulation());
        locationCard.setUncommonness(cardDTO.getUncommonness());

        if (! urlValidator.isValid(cardDTO.getPhotoUrl().toString()))
            throw new UserInfoInvalidException("Invalid photo URL");

        locationCard.setPhotoURL(cardDTO.getPhotoUrl());


        locationCardRepository.save(locationCard);
    }

    public List<CardDTO> getCardCollection() {
        return locationCardRepository.findAll().stream()
                .map(CardService::createCardDTO)
                .collect(Collectors.toList());
    }

    public void deleteLocationCard(long locationCardId) {
        locationCardRepository.deleteById(locationCardId);
    }

    private static CardDTO createCardDTO(LocationCard locationCard) {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(locationCard.getId());
        cardDTO.setName(locationCard.getName());
        cardDTO.setDescription(locationCard.getDescription());
        cardDTO.setPhotoUrl(locationCard.getPhotoURL());
        cardDTO.setLocation(getLocationString(locationCard.getLocation()));
        cardDTO.setCreatedBy(getCreatedBy(locationCard.getCreatedBy()));
        if (locationCard.getUncommonness() != null)
            cardDTO.setUncommonness(locationCard.getUncommonness() % 11);
        if (locationCard.getDifficulty() != null)
            cardDTO.setDifficulty(locationCard.getDifficulty() % 11);
        if (locationCard.getPopulation() != null)
            cardDTO.setPopulation(locationCard.getPopulation() % 11);
        cardDTO.setCreatedTime(getTime(locationCard.getCreatedDate()));
        if (locationCard.getLocation() != null) {
            cardDTO.setLatitude(locationCard.getLocation().getX());
            cardDTO.setLongitude(locationCard.getLocation().getY());
        }

        return cardDTO;
    }

    static String getLocationString(Point2D.Double point) {
        if (point == null) return "unknown";
        return point.getX() + ", " + point.getY();
    }

    static Point2D.Double parseLocationString(String locationString) {
        var array = locationString.split("\\s+");

        if (array.length != 2) throw new IllegalArgumentException("2 coordinates missing!");

        return new Point2D.Double(Double.parseDouble(array[0]), Double.parseDouble(array[1]));
    }

    private static String getCreatedBy(User user) {
        if (user == null) return "unknown";
        else return user.getUsername();
    }

    static String getTime(Instant instant) {
        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.systemDefault());

        return DATE_TIME_FORMATTER.format(instant);
    }

    public void collectLocationCard(Long id) {
        LocationCard card = locationCardRepository.getLocationCardById(id).orElseThrow();
        User user = authService.getCurrentUser();

        //Vec ima kartu
        if(userCardRepository.findById(new UserCardId(user.getUserId(), card.getId())).isPresent())
            return;

        var userLocation = Objects.requireNonNull(user.getCurrentLocation());
        var cardLocation = Objects.requireNonNull(card.getLocation());

        if (LocationService.calculateDistance(userLocation.getX(), cardLocation.getX(), userLocation.getY(), cardLocation.getY()) > MAX_CARD_DISTANCE) {
            throw new SpringGeoFighterException("Location is too far!");
        }

        var uc = new UserCard();
        uc.setUser(user);
        uc.setLocationCard(card);
        userCardRepository.save(uc);
    }

    public List<CardDTO> getNearbyCards() {
        User user = authService.getCurrentUser();
        var userLocation = Objects.requireNonNull(user.getCurrentLocation());

        return locationCardRepository.findAll().stream()
                .filter(l -> userCardRepository.findById(new UserCardId(user.getUserId(), l.getId())).isEmpty())
                .filter(l -> l.getLocation() != null)
                .filter(l -> {
                    var cardLocation = l.getLocation();

                    return LocationService.calculateDistance(userLocation.getX(), cardLocation.getX(), userLocation.getY(), cardLocation.getY()) <= MAX_CARD_DISTANCE;
                })
                .map(CardService::createCardDTO)
                .collect(Collectors.toList());
    }

    public void editLocatioCard(CardDTO card) {
        LocationCard locationCard = locationCardRepository.getLocationCardById(card.getId()).orElseThrow(
                () -> new SpringGeoFighterException("Card does not exist"));

        locationCard.setName(card.getName());
        locationCard.setDescription(card.getDescription());
        locationCard.setPhotoURL(card.getPhotoUrl());
        locationCard.setUncommonness(card.getUncommonness());
        locationCard.setDifficulty(card.getDifficulty());
        locationCard.setPopulation(card.getPopulation());

    }
}
