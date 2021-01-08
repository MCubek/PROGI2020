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
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

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


        createDefaultUsersIfNotFound();
        User admin = userRepository.findByUsername("admin").orElseThrow();

        createDefaultCardsIfNotFound(admin);

        loadCards(admin);

        alreadySetup = true;
    }

    @Transactional
    void loadCards(User defaultUser) {
        try {
            parseLocationCards(defaultUser).stream()
                    .filter(locationCard -> locationCardRepository.findByName(locationCard.getName()).isEmpty())
                    .forEach(locationCardRepository::save);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

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
        boolean local = address.equals("http://localhost:8080/");

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

        //Ostali se stvaraju samo kada se lokalno pokrene
        if (! local) return;

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
    void createDefaultCardsIfNotFound(User defaultUser) {
        boolean local = address.equals("http://localhost:8080/");

        User cart = null;
        User userA = null;
        User userB = null;
        User user = null;

        if (local) {
            cart = userRepository.findByUsername("card").orElseThrow(() -> new SpringGeoFighterException("No card user in database"));
            userA = userRepository.findByUsername("userA").orElseThrow(() -> new SpringGeoFighterException("No userA in database"));
            userB = userRepository.findByUsername("userB").orElseThrow(() -> new SpringGeoFighterException("No userB in database"));
            user = userRepository.findByUsername("user").orElseThrow(() -> new SpringGeoFighterException("No user in database"));
        }

        LocationCard l;
        UserCard uc;

        if (locationCardRepository.findByName("Sljeme").isEmpty()) {
            l = new LocationCard();
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
            l.setLocation(new Point2D.Double(45.89941353227559, 15.947867270990336));
            l.setAccepted(true);
            l.setAcceptedBy(local ? cart : defaultUser);
            l.setCreatedBy(local ? cart : defaultUser);

            l = locationCardRepository.save(l);

            if (local) {
                if (userCardRepository.findById(new UserCardId(userA.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(userA);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
                if (userCardRepository.findById(new UserCardId(userB.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(userB);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
                if (userCardRepository.findById(new UserCardId(user.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(user);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
            }
        }

        if (locationCardRepository.findByName("Trg Bana Jelačića").isEmpty()) {
            l = new LocationCard();
            l.setName("Trg Bana Jelačića");
            try {
                l.setPhotoURL(new URL("https://www.zagrebonline.hr/wp-content/uploads/2012/03/trga-bana-jelacica.jpg"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            l.setDescription("Trg bana Josipa Jelačića glavni je zagrebački trg. Njime dominira kip bana Josipa Jelačića kipara Dominika Fernkorna, danas okrenut prema jugu. Od nakon Drugoga svjetskoga rata do 1990. godine, kada mu je vraćeno staro ime, trg je nosio ime Trg Republike.");
            l.setNeedsToBeChecked(false);
            l.setPopulation(0);
            l.setUncommonness(6);
            l.setDifficulty(6);
            l.setLocation(new Point2D.Double(45.813122071220235, 15.977104348245451));
            l.setAccepted(true);
            l.setAcceptedBy(local ? cart : defaultUser);
            l.setCreatedBy(local ? cart : defaultUser);

            l = locationCardRepository.save(l);

            if (local) {
                if (userCardRepository.findById(new UserCardId(userA.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(userA);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
                if (userCardRepository.findById(new UserCardId(userB.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(userB);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
                if (userCardRepository.findById(new UserCardId(user.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(user);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
            }
        }

        if (locationCardRepository.findByName("Zagrebačka katedrala").isEmpty()) {
            l = new LocationCard();
            l.setName("Zagrebačka katedrala");
            try {
                l.setPhotoURL(new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b9/Zagreb_Cathedral_2020.jpg/390px-Zagreb_Cathedral_2020.jpg"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            l.setDescription("Katedrala Uznesenja Blažene Djevice Marije i svetih Stjepana i Ladislava ili Zagrebačka katedrala, najveća je hrvatska sakralna građevina i jedan od najvrjednijih spomenika hrvatske kulturne baštine. Prva je i najznačajnija gotička građevina Hrvatske. Najmonumentalija je gotička sakralna građevina jugoistočno od Alpa.");
            l.setNeedsToBeChecked(false);
            l.setLocation(new Point2D.Double(45.81409233434517, 15.979440552226094));
            l.setPopulation(0);
            l.setUncommonness(7);
            l.setDifficulty(7);
            l.setAccepted(true);
            l.setAcceptedBy(local ? cart : defaultUser);
            l.setCreatedBy(local ? cart : defaultUser);

            l = locationCardRepository.save(l);

            if (local) {
                if (userCardRepository.findById(new UserCardId(userA.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(userA);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
                if (userCardRepository.findById(new UserCardId(userB.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(userB);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
                if (userCardRepository.findById(new UserCardId(user.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(user);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
            }
        }

        if (locationCardRepository.findByName("Crkva sv. Marka").isEmpty()) {
            l = new LocationCard();
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
            l.setLocation(new Point2D.Double(45.816090294051854, 15.9736456397399));
            l.setDifficulty(4);
            l.setAccepted(true);
            l.setAcceptedBy(local ? cart : defaultUser);
            l.setCreatedBy(local ? cart : defaultUser);

            l = locationCardRepository.save(l);

            if (local) {
                if (userCardRepository.findById(new UserCardId(userA.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(userA);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
                if (userCardRepository.findById(new UserCardId(userB.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(userB);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
                if (userCardRepository.findById(new UserCardId(user.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(user);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
            }
        }

        if (locationCardRepository.findByName("Hrvatsko Narodno Kazalište").isEmpty()) {
            l = new LocationCard();
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
            l.setLocation(new Point2D.Double(45.80952550045766, 15.970039409659902));
            l.setAccepted(true);
            l.setAcceptedBy(local ? cart : defaultUser);
            l.setCreatedBy(local ? cart : defaultUser);

            l = locationCardRepository.save(l);

            if (local) {
                if (userCardRepository.findById(new UserCardId(userA.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(userA);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
                if (userCardRepository.findById(new UserCardId(userB.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(userB);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
                if (userCardRepository.findById(new UserCardId(user.getUserId(), l.getId())).isEmpty()) {
                    uc = new UserCard();
                    uc.setUser(user);
                    uc.setLocationCard(l);
                    userCardRepository.save(uc);
                }
            }
        }

        if (! local) return;

        if (locationCardRepository.getLocationCardsByName("Jarun Veliko Jezero").isEmpty()) {
            l = new LocationCard();
            l.setName("Jarun Veliko Jezero");
            try {
                l.setPhotoURL(new URL("https://upload.wikimedia.org/wikipedia/commons/1/1b/Jarun_Lake_aerial_view.jpg"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            l.setDescription("Veliko jarunsko jezero");
            l.setNeedsToBeChecked(true);
            l.setPopulation(0);
            l.setUncommonness(8);
            l.setDifficulty(8);
            l.setAccepted(false);
            l.setLocation(new Point2D.Double(45.783333, 15.916667));
            l.setCreatedBy(userA);

            locationCardRepository.save(l);
        }

        if (locationCardRepository.getLocationCardsByName("Jakuševec").isEmpty()) {
            l = new LocationCard();
            l.setName("Jakuševec");
            try {
                l.setPhotoURL(new URL("https://www.kronikevg.com/wp-content/uploads/2014/01/jakusevec.jpg"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            l.setDescription("Skijalište i odlagalište otpada.");
            l.setNeedsToBeChecked(true);
            l.setPopulation(0);
            l.setUncommonness(8);
            l.setDifficulty(10);
            l.setAccepted(false);
            l.setLocation(new Point2D.Double(45.766667, 16.016667));
            l.setCreatedBy(userB);

            locationCardRepository.save(l);
        }
    }

    private static List<LocationCard> parseLocationCards(User defaultOwner) throws IOException {
        Path file = Path.of("izvorniKod/geofighterSpring/src/main/resources/cards.txt");

        return Arrays.stream(Files.readString(file, StandardCharsets.UTF_8).split("\r?\n[\n\r]+"))
                .map(loc -> {
                    var lines = Arrays.stream(loc.split("\n"))
                            .filter(l -> ! l.isBlank())
                            .map(line -> line.substring(line.indexOf("=") + 1).trim())
                            .collect(Collectors.toList());

                    var card = new LocationCard();
                    card.setName(lines.get(0));
                    card.setDescription(lines.get(1));
                    try {
                        card.setPhotoURL(new URL(lines.get(2)));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    card.setLocation(new Point2D.Double(Double.parseDouble(lines.get(3)), Double.parseDouble(lines.get(4))));
                    card.setPopulation(Integer.valueOf(lines.get(5)));
                    card.setUncommonness(Integer.valueOf(lines.get(6)));
                    card.setDifficulty(Integer.valueOf(lines.get(7)));
                    card.setCreatedBy(defaultOwner);
                    card.setAcceptedBy(defaultOwner);
                    card.setAccepted(true);
                    return card;
                }).collect(Collectors.toList());
    }
}
