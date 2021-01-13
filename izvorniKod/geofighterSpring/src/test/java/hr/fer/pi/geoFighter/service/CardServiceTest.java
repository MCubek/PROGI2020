package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.LocationCard;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.model.UserCard;
import hr.fer.pi.geoFighter.model.UserCardId;
import hr.fer.pi.geoFighter.repository.LocationCardRepository;
import hr.fer.pi.geoFighter.repository.UserCardRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.*;

import java.awt.geom.Point2D;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
/**
 * @author MatejCubek
 * @project pi
 * @created 11/01/2021
 */
@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private LocationCardRepository locationCardRepository;

    @Mock
    private UserCardRepository userCardRepository;

    @Mock
    private AuthService authService;

    private LocationCard locationCard;

    private User currentUser;

    @Test
    void getLocationString() {
        var point = new Point2D.Double(11.11, 12.12);
        assertEquals("11.11, 12.12", CardService.getLocationString(point));
    }

    @Test
    void parseLocationString() {
        var locationString = "11.11 12.12";

        assertEquals(new Point2D.Double(11.11, 12.12), CardService.parseLocationString(locationString));
    }

    @Test
    void getTime() {
        Instant time = Instant.EPOCH;

        assertEquals("1970-01-01 01:00", CardService.getTime(time));
    }

    @Test
    void collectLocationCardThrowsErrorIfUserIsTooFar() {
        locationCard = new LocationCard();
        locationCard.setLocation(new Point2D.Double(0, 0));
        locationCard.setId(1L);
        locationCard.setName("card");

        currentUser = new User();
        currentUser.setUserId(1L);
        currentUser.setUsername("user");

        when(locationCardRepository.getLocationCardById(1L)).thenReturn(Optional.of(locationCard));
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(userCardRepository.findById(new UserCardId(currentUser.getUserId(), locationCard.getId()))).thenReturn(Optional.empty());

        currentUser.setCurrentLocation(new Point2D.Double(15, 15));

        Assertions.assertThrows(SpringGeoFighterException.class, () -> cardService.collectLocationCard(1L));
    }

    @Test
    void collectLocationCardSavesWhenValid() {
        locationCard = new LocationCard();
        locationCard.setLocation(new Point2D.Double(0, 0));
        locationCard.setId(1L);
        locationCard.setName("card");

        currentUser = new User();
        currentUser.setUserId(1L);
        currentUser.setUsername("user");

        when(locationCardRepository.getLocationCardById(1L)).thenReturn(Optional.of(locationCard));
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(userCardRepository.findById(new UserCardId(currentUser.getUserId(), locationCard.getId()))).thenReturn(Optional.empty());

        currentUser.setCurrentLocation(new Point2D.Double(0.01, 0.01));

        cardService.collectLocationCard(1L);

        var uc = new UserCard();
        uc.setLocationCard(locationCard);
        uc.setUser(currentUser);

        verify(userCardRepository).save(uc);
    }

    @Test
    void collectLocationCardDoesNothingWhenUserHasCard() {
        locationCard = new LocationCard();
        locationCard.setLocation(new Point2D.Double(0, 0));
        locationCard.setId(1L);
        locationCard.setName("card");

        currentUser = new User();
        currentUser.setUserId(1L);
        currentUser.setUsername("user");

        when(locationCardRepository.getLocationCardById(1L)).thenReturn(Optional.of(locationCard));
        when(authService.getCurrentUser()).thenReturn(currentUser);
        when(userCardRepository.findById(new UserCardId(currentUser.getUserId(), locationCard.getId()))).thenReturn(Optional.empty());

        currentUser.setCurrentLocation(new Point2D.Double(0.01, 0.01));

        var uc = new UserCard();
        uc.setLocationCard(locationCard);
        uc.setUser(currentUser);

        when(userCardRepository.findById(new UserCardId(currentUser.getUserId(), locationCard.getId()))).thenReturn(Optional.of(uc));

        cardService.collectLocationCard(1L);


        verify(userCardRepository, times(0)).save(uc);
    }

    @Test
    @Disabled("Not implemented yet")
    void testSomethingNotImplemented(){
        Assertions.assertTrue(cardService.somethingNotImplemented());
    }

}