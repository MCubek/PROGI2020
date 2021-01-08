package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.CardDTO;
import hr.fer.pi.geoFighter.dto.CardApplicationDTO;
import hr.fer.pi.geoFighter.dto.CartographerUserDTO;
import hr.fer.pi.geoFighter.dto.DisableUserDTO;
import hr.fer.pi.geoFighter.dto.UserDTO;
import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.CartographerStatus;
import hr.fer.pi.geoFighter.model.LocationCard;
import hr.fer.pi.geoFighter.model.Role;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.repository.LocationCardRepository;
import hr.fer.pi.geoFighter.repository.RoleRepository;
import hr.fer.pi.geoFighter.repository.UserRepository;
import hr.fer.pi.geoFighter.util.ImageValidateUtility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LocationCardRepository locationCardRepository;

    public List<CartographerUserDTO> getCartographerApplications() {
        List<CartographerUserDTO> usernames = new ArrayList<>();
        for (User user : userRepository.findUsersByCartographerStatus(CartographerStatus.APPLIED)) {
            usernames.add(new CartographerUserDTO
                    (user.getUsername(), user.getEmail(), Date.from(user.getCreatedTime()), user.getPhotoURL(), user.getEloScore(), user.getIdCardPhotoURL()));

        }

        return usernames;
    }

    public void acceptCartographerApplication(String username) {
        System.out.println(username);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new SpringGeoFighterException("User does not exist"));

        if(user.getCartographerStatus()!=CartographerStatus.APPLIED)
            throw new SpringGeoFighterException("User has not applied!");

        user.setCartographerStatus(CartographerStatus.APPROVED);

        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new SpringGeoFighterException("Can't find role!"));
        Role cartographerRole = roleRepository.findByName("ROLE_CARTOGRAPHER").orElseThrow(() -> new SpringGeoFighterException("Can't find role!"));

        //Je li uloga user. ne zelimo adminima oduzeti prava.
        if (user.getRole().getRoleId().equals(userRole.getRoleId())) {
            user.setRole(cartographerRole);
        }
    }

    public List<String> getEnabledUsernames() {
        return userRepository.findUsersByEnabledTrueOrderByUsernameAsc().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    public void promoteToAdmin(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringGeoFighterException("User does not exist"));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new SpringGeoFighterException("Can't find role!"));

        user.setRole(adminRole);
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringGeoFighterException("User does not exist"));

        userRepository.delete(user);
    }

    public void disableUser(DisableUserDTO disableUserDTO) {
        User user = userRepository.findByUsername(disableUserDTO.getUsername()).orElseThrow(() -> new SpringGeoFighterException("User does not exist"));

        LocalDateTime timeoutEnd = LocalDateTime.parse(
                disableUserDTO.getTimeoutEnd(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        );
        if(timeoutEnd.isBefore(LocalDateTime.now()))
            throw new SpringGeoFighterException("Timeout end cannot be a past date");

        user.setForcedTimeoutEnd(timeoutEnd);
    }

    public List<CardDTO> getCardCollection() {
        return locationCardRepository.findAll().stream()
                .map(CardService::createCardDTO)
                .collect(Collectors.toList());
    }

    public void editLocationCard(CardDTO card) {
        LocationCard locationCard = locationCardRepository.getLocationCardById(card.getId()).orElseThrow(
                () -> new SpringGeoFighterException("Card does not exist"));

        ImageValidateUtility.validateImage(card.getPhotoURL());

        locationCard.setName(card.getName());
        locationCard.setDescription(card.getDescription());
        locationCard.setPhotoURL(card.getPhotoURL());
        locationCard.setUncommonness(card.getUncommonness());
        locationCard.setDifficulty(card.getDifficulty());
        locationCard.setPopulation(card.getPopulation());

    }

    public void deleteLocationCard(long locationCardId) {
        locationCardRepository.deleteById(locationCardId);
    }

    public void editUser(UserDTO userDTO) {

        System.out.println(userDTO.getUsername());
        User user = userRepository.findByUsername(userDTO.getUsername()).orElseThrow(
                () -> new SpringGeoFighterException("User does not exist"));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
    }

    public List<UserDTO> getAllUserModels() {
        List<UserDTO> userCollection = new ArrayList<>();

        for(User user : userRepository.findAll()){
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userCollection.add(userDTO);
        }

        return userCollection;
    }

}
