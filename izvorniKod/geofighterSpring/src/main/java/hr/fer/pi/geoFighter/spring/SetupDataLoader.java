package hr.fer.pi.geoFighter.spring;

import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.model.*;
import hr.fer.pi.geoFighter.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;
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
    private final LocationCardRepository locationCardRepository;
    private final UserCardRepository userCardRepository;
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
            createDefaultCardsIfNotFound();
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

    @Transactional
    void createDefaultCardsIfNotFound() {
        User cart = userRepository.findByUsername("card").get();

        LocationCard l;
        UserCard uc;

        User user = userRepository.findByUsername("user").get();

        if(locationCardRepository.findById(1L).isEmpty()){
            l = new LocationCard();
            l.setId(1L);
            l.setName("Sljeme");
            try {
                l.setPhotoURL(new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/Sljeme.jpg/435px-Sljeme.jpg"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            l.setDescription("Sljeme je vrh Medvednice, planine iznad Zagreba. Nalazi se na 1033 metara nadmorske visine. Do Sljemena vodi asfaltirana cesta, mnogobrojne pješačke staze, a od 1963. do 2007. u pogonu je bila i sljemenska žičara. Sljeme je jedno od najdražih izletišta Zagrepčana.Na Sljemenu se nalazi i skijaška staza koja je jedna od lokacija za utrke svjetskog skijaškog kupa (Snježna kraljica). Na samom vrhu nalazi se RTV toranj, visok 169 metara koji je izgrađen 1973.");
            l.setNeedsToBeChecked(false);
            l.setPopulation(0);
            l.setUncommonness(5);
            l.setDifficulty(5);
            l.setAccepted(true);
            l.setAcceptedBy(cart);

            locationCardRepository.save(l);

            if(userCardRepository.findById(1L).isEmpty()) {
                uc = new UserCard();
                uc.setUser(user);
                uc.setLocationCard(l);
                uc.setCooldownMultiplier(1.1);
                userCardRepository.save(uc);
                user.getLocationCardAssoc().add(uc);
            }
        }

        if(locationCardRepository.findById(2L).isEmpty()){
            l = new LocationCard();
            l.setId(2L);
            l.setName("Trg Bana Jelačića");
            try {
                l.setPhotoURL(new URL("https://hr.wikipedia.org/wiki/Datoteka:Trg_bana_Jelacica_Zagreb_30102012_2_roberta_f.jpg"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            l.setDescription("Trg bana Josipa Jelačića glavni je zagrebački trg. Njime dominira kip bana Josipa Jelačića kipara Dominika Fernkorna, danas okrenut prema jugu. Od nakon Drugoga svjetskoga rata do 1990. godine, kada mu je vraćeno staro ime, trg je nosio ime Trg Republike.");
            l.setNeedsToBeChecked(false);
            l.setPopulation(0);
            l.setUncommonness(6);
            l.setDifficulty(6);
            l.setAccepted(true);
            l.setAcceptedBy(cart);

            locationCardRepository.save(l);

            if(userCardRepository.findById(1L).isEmpty()) {
                uc = new UserCard();
                uc.setUser(user);
                uc.setLocationCard(l);
                uc.setCooldownMultiplier(1.1);
                userCardRepository.save(uc);
                user.getLocationCardAssoc().add(uc);
            }
        }

        if(locationCardRepository.findById(3L).isEmpty()){
            l = new LocationCard();
            l.setId(3L);
            l.setName("Zagrebačka katedrala");
            try {
                l.setPhotoURL(new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b9/Zagreb_Cathedral_2020.jpg/390px-Zagreb_Cathedral_2020.jpg"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            l.setDescription("Katedrala Uznesenja Blažene Djevice Marije i svetih Stjepana i Ladislava ili Zagrebačka katedrala, najveća je hrvatska sakralna građevina i jedan od najvrjednijih spomenika hrvatske kulturne baštine. Prva je i najznačajnija gotička građevina Hrvatske. Najmonumentalija je gotička sakralna građevina jugoistočno od Alpa.");
            l.setNeedsToBeChecked(false);
            l.setPopulation(0);
            l.setUncommonness(7);
            l.setDifficulty(7);
            l.setAccepted(true);
            l.setAcceptedBy(cart);

            locationCardRepository.save(l);

            if(userCardRepository.findById(1L).isEmpty()) {
                uc = new UserCard();
                uc.setUser(user);
                uc.setLocationCard(l);
                uc.setCooldownMultiplier(1.1);
                userCardRepository.save(uc);
                user.getLocationCardAssoc().add(uc);
            }
        }

        if(locationCardRepository.findById(4L).isEmpty()){
            l = new LocationCard();
            l.setId(4L);
            l.setName("Crkva sv. Marka");
            try {
                l.setPhotoURL(new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/St_Marks_Church_Zagreb.jpg/450px-St_Marks_Church_Zagreb.jpg"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            l.setDescription("Crkva svetoga Marka jedan je od najstarijih građevnih spomenika grada Zagreba. Smještena je na Trgu svetoga Marka, kao i Hrvatski sabor. Crkva sv. Marka Evanđelista zaštićeno je kulturno dobro Republike Hrvatske.");
            l.setNeedsToBeChecked(false);
            l.setPopulation(0);
            l.setUncommonness(4);
            l.setDifficulty(4);
            l.setAccepted(true);
            l.setAcceptedBy(cart);

            locationCardRepository.save(l);

            if(userCardRepository.findById(1L).isEmpty()) {
                uc = new UserCard();
                uc.setUser(user);
                uc.setLocationCard(l);
                uc.setCooldownMultiplier(1.1);
                userCardRepository.save(uc);
                user.getLocationCardAssoc().add(uc);
            }
        }

        if(locationCardRepository.findById(5L).isEmpty()){
            l = new LocationCard();
            l.setId(5L);
            l.setName("Hrvatsko Narodno Kazalište");
            try {
                l.setPhotoURL(new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/0/01/Hrvatsko_narodno_kazaliste_u_Zagrebu_090609.jpg/330px-Hrvatsko_narodno_kazaliste_u_Zagrebu_090609.jpg"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            l.setDescription("Dana 14. listopada 1895. u Zagrebu svečano je otvorena kazališna zgrada za oko 750 gledatelja u kojoj danas djeluje Hrvatsko narodno kazalište. Neobarokna zgrada HNK je remek-djelo kasnog historizma austrijskog arhitekta Ferdinanda Fellnera i njemačkog arhitekta Hermanna Helmera.");
            l.setNeedsToBeChecked(false);
            l.setPopulation(0);
            l.setUncommonness(8);
            l.setDifficulty(8);
            l.setAccepted(true);
            l.setAcceptedBy(cart);

            locationCardRepository.save(l);

            if(userCardRepository.findById(1L).isEmpty()) {
                uc = new UserCard();
                uc.setUser(user);
                uc.setLocationCard(l);
                uc.setCooldownMultiplier(1.1);
                userCardRepository.save(uc);
                user.getLocationCardAssoc().add(uc);
            }
        }
    }
}
