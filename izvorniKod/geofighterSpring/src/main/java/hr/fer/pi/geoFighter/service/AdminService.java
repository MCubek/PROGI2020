package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.CartographerUserDTO;
import hr.fer.pi.geoFighter.dto.DisableUserDTO;
import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.CartographerStatus;
import hr.fer.pi.geoFighter.model.Role;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.repository.RoleRepository;
import hr.fer.pi.geoFighter.repository.UserRepository;
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
        return userRepository.findUsersByEnabledTrue().stream()
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

}
