package hr.fer.pi.geoFighter.spring;

import hr.fer.pi.geoFighter.model.Privilege;
import hr.fer.pi.geoFighter.model.Role;
import hr.fer.pi.geoFighter.repository.PrivilegeRepository;
import hr.fer.pi.geoFighter.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup;

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

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
}
