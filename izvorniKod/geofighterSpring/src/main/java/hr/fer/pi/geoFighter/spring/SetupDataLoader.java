package hr.fer.pi.geoFighter.spring;

import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
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
        User user = new User();
        if (userRepository.findByUsername("admin").isEmpty()) {
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
            user.setRole(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new SpringGeoFighterException("ADMIN role not in database")));
            user.setEnabled(true);

            userRepository.save(user);
        }


        if (userRepository.findByUsername("card").isEmpty()) {
            user = new User();
            user.setUsername("card");
            user.setEmail("card@card.com");
            user.setPassword(passwordEncoder.encode("card"));
            user.setRole(roleRepository.findByName("ROLE_CARTOGRAPHER").orElseThrow(() -> new SpringGeoFighterException("ADMIN role not in database")));
            user.setEnabled(true);

            userRepository.save(user);
        }
    }
}