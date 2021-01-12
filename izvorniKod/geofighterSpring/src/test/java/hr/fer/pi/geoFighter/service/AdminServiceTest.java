package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.CardDTO;
import hr.fer.pi.geoFighter.dto.UserDTO;
import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.CartographerStatus;
import hr.fer.pi.geoFighter.model.LocationCard;
import hr.fer.pi.geoFighter.model.Role;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.repository.LocationCardRepository;
import hr.fer.pi.geoFighter.repository.RoleRepository;
import hr.fer.pi.geoFighter.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import static java.util.Optional.of;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author MatejCubek
 * @project pi
 * @created 11/01/2021
 */
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private LocationCardRepository locationCardRepository;

    private User cartographerApplicant;

    private Role adminRole;
    private Role cartographerRole;
    private Role userRole;

    @BeforeEach
    void initUser() {
        cartographerApplicant = new User();
        cartographerApplicant.setUsername("applicant");
        cartographerApplicant.setEmail("applicant@gmail.com");
        cartographerApplicant.setCartographerStatus(CartographerStatus.APPLIED);

        adminRole = new Role();
        adminRole.setRoleId(3L);
        adminRole.setName("ROLE_ADMIN");

        cartographerRole = new Role();
        cartographerRole.setRoleId(1L);
        cartographerRole.setName("ROLE_CARTOGRAPHER");

        userRole = new Role();
        userRole.setRoleId(2L);
        userRole.setName("ROLE_USER");

        cartographerApplicant.setRole(userRole);
    }

    @Test
    void getCartographerApplications() throws MalformedURLException {
        User applicant2 = new User();
        applicant2.setUsername("Steve");
        applicant2.setEmail("steve@gmail.com");
        applicant2.setIban("HR3824840089434722222");
        applicant2.setIdCardPhotoURL(new URL("https://i.ytimg.com/vi/-AVloqiXdk0/maxresdefault.jpg"));

        when(userRepository.findUsersByCartographerStatus(CartographerStatus.APPLIED)).thenReturn(List.of(cartographerApplicant, applicant2));

        var applications = adminService.getCartographerApplications();

        assertEquals("applicant", applications.get(0).getUsername());
        assertEquals("applicant@gmail.com", applications.get(0).getEmail());

        assertEquals("Steve", applications.get(1).getUsername());
        assertEquals("steve@gmail.com", applications.get(1).getEmail());
        assertEquals("HR3824840089434722222", applications.get(1).getIBAN());
        assertEquals(new URL("https://i.ytimg.com/vi/-AVloqiXdk0/maxresdefault.jpg"), applications.get(1).getIdUrl());

    }

    @Test
    void acceptCartographerApplication() {
        when(userRepository.findByUsername(cartographerApplicant.getUsername())).thenReturn(of(cartographerApplicant));
        when(userRepository.findById(1L)).thenReturn(of(cartographerApplicant));
        when(userRepository.findById(1L)).thenReturn(of(cartographerApplicant));
        when(roleRepository.findByName("ROLE_CARTOGRAPHER")).thenReturn(of(cartographerRole));
        when(roleRepository.findByName("ROLE_USER")).thenReturn(of(userRole));

        adminService.acceptCartographerApplication(cartographerApplicant.getUsername());

        assertEquals(
                CartographerStatus.APPROVED,
                userRepository.findById(1L).get().getCartographerStatus());

        assertEquals(
                "ROLE_CARTOGRAPHER",
                userRepository.findById(1L).get().getRole().getName());
    }

    @Test
    void declineCartographerRequest() {
        when(userRepository.findByUsername("applicant")).thenReturn(of(cartographerApplicant));

        adminService.declineCartographerApplication("applicant");

        assertEquals(CartographerStatus.NOT_REQUESTED, cartographerApplicant.getCartographerStatus());
    }

    @Test
    void declineCartographerRequest_userNotApplied_throwException() {
        User user = new User();
        user.setUsername("notApplicant");
        user.setCartographerStatus(CartographerStatus.NOT_REQUESTED);

        when(userRepository.findByUsername("notApplicant")).thenReturn(of(user));

        assertThrows(SpringGeoFighterException.class, () -> adminService.declineCartographerApplication("notApplicant"));
    }

    @Test
    void promoteToAdmin() {

        User user = new User();
        user.setUsername("user");
        user.setRole(userRole);

        when(userRepository.findByUsername("user")).thenReturn(of(user));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(of(adminRole));

        adminService.promoteToAdmin("user");

        assertEquals("ROLE_ADMIN", user.getRole().getName());
    }

    @Test
    void demoteFromAdmin() {

        User user = new User();
        user.setUsername("user");
        user.setRole(adminRole);

        when(userRepository.findByUsername("user")).thenReturn(of(user));
        when(roleRepository.findByName("ROLE_USER")).thenReturn(of(userRole));

        adminService.demoteFromAdmin("user");

        assertEquals("ROLE_USER", user.getRole().getName());
    }

    @Test
    void getCardCollection() {
        LocationCard card1 = mock(LocationCard.class);
        LocationCard card2 = mock(LocationCard.class);
        LocationCard card3 = mock(LocationCard.class);

        when(locationCardRepository.findAll()).thenReturn(List.of(card1, card2, card3));
        when(card1.getCreatedDate()).thenReturn(Instant.now());
        when(card2.getCreatedDate()).thenReturn(Instant.now());
        when(card3.getCreatedDate()).thenReturn(Instant.now());

        List<CardDTO> locationCards = adminService.getCardCollection();

        assertEquals(3, locationCards.size());
    }

    @Test
    void editLocationCard() throws MalformedURLException {
        LocationCard locationCard = new LocationCard();
        locationCard.setName("name");
        locationCard.setDescription("description");
        locationCard.setPhotoURL(new URL("https://blabla.jpg"));
        locationCard.setUncommonness(2);
        locationCard.setDifficulty(2);
        locationCard.setPopulation(2);

        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(1L);
        cardDTO.setName("modified name");
        cardDTO.setDescription("modified description");
        cardDTO.setPhotoURL(new URL("https://i.ytimg.com/vi/-AVloqiXdk0/maxresdefault.jpg"));
        cardDTO.setUncommonness(5);
        cardDTO.setDifficulty(5);
        cardDTO.setPopulation(5);

        when(locationCardRepository.getLocationCardById(1L)).thenReturn(of(locationCard));

        adminService.editLocationCard(cardDTO);

        assertEquals("modified name", locationCard.getName());
        assertEquals("modified description", locationCard.getDescription());
        assertEquals(new URL("https://i.ytimg.com/vi/-AVloqiXdk0/maxresdefault.jpg"), locationCard.getPhotoURL());
        assertEquals(5, locationCard.getUncommonness());
        assertEquals(5, locationCard.getDifficulty());
        assertEquals(5, locationCard.getPopulation());

    }

    @Test
    void editLocationCard_badUrl_throwException() throws MalformedURLException {
        LocationCard locationCard = new LocationCard();
        locationCard.setPhotoURL(new URL("https://i.ytimg.com/vi/-AVloqiXdk0/maxresdefault.jpg"));

        CardDTO cardDTO = new CardDTO();
        cardDTO.setId(1L);
        cardDTO.setPhotoURL(new URL("https://blabla.jpg"));

        when(locationCardRepository.getLocationCardById(1L)).thenReturn(of(locationCard));

        assertThrows(SpringGeoFighterException.class, () -> adminService.editLocationCard(cardDTO));
    }

    @Test
    void editUser() throws MalformedURLException {

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@user.com");
        user.setPhotoURL(new URL("https://blabla.jpg"));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("newuser");
        userDTO.setEmail("newuser@user.com");
        userDTO.setPhotoURL(new URL("https://i.ytimg.com/vi/-AVloqiXdk0/maxresdefault.jpg"));

        when(userRepository.findById(1L)).thenReturn(of(user));

        adminService.editUser(userDTO);

        assertEquals("newuser", user.getUsername());
        assertEquals("newuser@user.com", user.getEmail());
        assertEquals(new URL("https://i.ytimg.com/vi/-AVloqiXdk0/maxresdefault.jpg"), user.getPhotoURL());

    }

    @Test
    void editUser_badUrl_throwException() throws MalformedURLException {

        User user = new User();
        user.setPhotoURL(new URL("https://i.ytimg.com/vi/-AVloqiXdk0/maxresdefault.jpg"));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setPhotoURL(new URL("https://blabla.jpg"));

        when(userRepository.findById(1L)).thenReturn(of(user));

        assertThrows(SpringGeoFighterException.class, () -> adminService.editUser(userDTO));
    }
}