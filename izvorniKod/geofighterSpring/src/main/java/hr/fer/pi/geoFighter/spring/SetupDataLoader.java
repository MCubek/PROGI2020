package hr.fer.pi.geoFighter.spring;

import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.CartographerStatus;
import hr.fer.pi.geoFighter.model.Privilege;
import hr.fer.pi.geoFighter.model.Role;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.repository.PrivilegeRepository;
import hr.fer.pi.geoFighter.repository.RoleRepository;
import hr.fer.pi.geoFighter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${server.developer.address}")
    private String address;

    boolean alreadySetup;

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        if (alreadySetup)
            return;

        Privilege userPrivilege
                = createPrivilegeIfNotFound("USER_PRIVILEGE");
        Privilege cartographerPrivilege
                = createPrivilegeIfNotFound("CARTOGRAPHER_PRIVILEGE");
        Privilege adminPrivilege
                = createPrivilegeIfNotFound("ADMIN_PRIVILEGE");

        createRoleIfNotFound("ROLE_USER", Collections.singletonList(userPrivilege));
        createRoleIfNotFound("ROLE_CARTOGRAPHER", Arrays.asList(userPrivilege, cartographerPrivilege));
        createRoleIfNotFound("ROLE_ADMIN", Arrays.asList(userPrivilege, cartographerPrivilege, adminPrivilege));

        if (address.equals("http://localhost:8080/")) {
            createDefaultUsersIfNotFound();
        }

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name).orElse(null);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    void createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name).orElse(null);
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
    }

    @Transactional
    void createDefaultUsersIfNotFound() {
        User user;
        if (userRepository.findByUsername("admin").isEmpty()) {
            user = new User();
            user.setUsername("admin");
            user.setEmail("admin@admin.com");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setRole(roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new SpringGeoFighterException("ADMIN role not in database")));
            user.setEnabled(true);

            userRepository.save(user);
        }

        if (userRepository.findByUsername("user").isEmpty()) {
            user = new User();
            user.setUsername("user");
            user.setEmail("user@user.com");
            user.setPassword(passwordEncoder.encode("user"));
            user.setRole(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new SpringGeoFighterException("USER role not in database")));
            user.setEnabled(true);

            userRepository.save(user);
        }


        if (userRepository.findByUsername("card").isEmpty()) {
            user = new User();
            user.setUsername("card");
            user.setEmail("card@card.com");
            user.setPassword(passwordEncoder.encode("card"));
            user.setRole(roleRepository.findByName("ROLE_CARTOGRAPHER").orElseThrow(() -> new SpringGeoFighterException("CARTOGRAPHER role not in database")));
            user.setEnabled(true);

            userRepository.save(user);
        }

        if (userRepository.findByUsername("userA").isEmpty()) {
            user = new User();
            user.setUsername("userA");
            user.setEmail("usera@usera.com");
            user.setPassword(passwordEncoder.encode("userA"));
            user.setRole(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new SpringGeoFighterException("USER role not in database")));
            user.setEloScore(1234);
            user.setWins(32);
            user.setLosses(24);
            user.setEnabled(true);

            userRepository.save(user);
        }

        if (userRepository.findByUsername("userB").isEmpty()) {
            user = new User();
            user.setUsername("userB");
            user.setEmail("userb@userb.com");
            user.setPassword(passwordEncoder.encode("userB"));
            user.setRole(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new SpringGeoFighterException("USER role not in database")));
            user.setEloScore(123);
            user.setWins(15);
            user.setLosses(16);
            user.setEnabled(true);

            userRepository.save(user);
        }

        if (userRepository.findByUsername("userC").isEmpty()) {
            user = new User();
            user.setUsername("userC");
            user.setEmail("userc@userc.com");
            user.setPassword(passwordEncoder.encode("userC"));
            user.setRole(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new SpringGeoFighterException("USER role not in database")));
            user.setEloScore(132);
            user.setWins(17);
            user.setLosses(15);
            user.setEnabled(true);

            userRepository.save(user);
        }

        if (userRepository.findByUsername("nepotvrdeni").isEmpty()) {
            user = new User();
            user.setUsername("nepotvrdeni");
            user.setEmail("nepotvrdeni@nepotvrdeni.com");
            user.setPassword(passwordEncoder.encode("nepotvrdeni"));
            user.setRole(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new SpringGeoFighterException("USER role not in database")));
            user.setEnabled(false);

            userRepository.save(user);
        }

        if (userRepository.findByUsername("applied").isEmpty()) {
            user = new User();
            user.setUsername("applied");
            user.setEmail("applied@applied.com");
            user.setPassword(passwordEncoder.encode("applied"));
            user.setRole(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new SpringGeoFighterException("USER role not in database")));
            user.setEnabled(true);
            user.setCartographerStatus(CartographerStatus.APPLIED);

            userRepository.save(user);
        }
    }
}
