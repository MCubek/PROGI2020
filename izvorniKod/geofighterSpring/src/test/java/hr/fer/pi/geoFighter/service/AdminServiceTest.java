package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.model.CartographerStatus;
import hr.fer.pi.geoFighter.model.Role;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.repository.RoleRepository;
import hr.fer.pi.geoFighter.repository.UserRepository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
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

    private User cartographerApplicant;

    private Role cartographerRole;
    private Role userRole;

    @BeforeEach
    void initUser() {
        cartographerApplicant = new User();
        cartographerApplicant.setUsername("applicant");
        cartographerApplicant.setCartographerStatus(CartographerStatus.APPLIED);

        cartographerRole = new Role();
        cartographerRole.setRoleId(1L);
        cartographerRole.setName("ROLE_CARTOGRAPHER");

        userRole = new Role();
        userRole.setRoleId(2L);
        userRole.setName("ROLE_USER");

        cartographerApplicant.setRole(userRole);
    }

    @Test
    void acceptCartographerApplication() {
        when(userRepository.findByUsername(cartographerApplicant.getUsername())).thenReturn(Optional.of(cartographerApplicant));
        when(userRepository.findById(1L)).thenReturn(Optional.of(cartographerApplicant));
        when(userRepository.findById(1L)).thenReturn(Optional.of(cartographerApplicant));
        when(roleRepository.findByName("ROLE_CARTOGRAPHER")).thenReturn(Optional.of(cartographerRole));
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));

        adminService.acceptCartographerApplication(cartographerApplicant.getUsername());

        assertEquals(
                CartographerStatus.APPROVED,
                userRepository.findById(1L).get().getCartographerStatus());

        assertEquals(
                "ROLE_CARTOGRAPHER",
                userRepository.findById(1L).get().getRole().getName());
    }
}