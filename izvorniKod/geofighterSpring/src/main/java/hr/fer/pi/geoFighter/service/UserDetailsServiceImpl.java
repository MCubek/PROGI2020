package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.model.Privilege;
import hr.fer.pi.geoFighter.model.Role;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional
                .orElseThrow(() -> new UsernameNotFoundException("No user " +
                        "Found with username : " + username));

        return new org.springframework.security
                .core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true,
                true, getAuthorities(user.getRole()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return getGrantedAuthorities(getPrivileges(role));
    }

    private List<String> getPrivileges(Role role) {

        List<String> privileges = new ArrayList<>();
        Collection<Privilege> collection = role.getPrivileges();

        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    @Transactional
    public void calculateEloScore(Long winnerId, Long loserId, int draw){
        //flag za draw
        User winner = getUser(winnerId);
        User loser = getUser(loserId);

        float score1;
        float score2;
        int K = 40;
        float expectancyA = 1 / (1 + (float) Math.pow(10.0, (winner.getEloScore() - loser.getEloScore()) / 400f));
        float expectancyB = 1 - expectancyA;

        if (draw == 0) {
            //calculating score in case of draw
            score1 = winner.getEloScore() + K * (0.5f - expectancyA);
            score2 = winner.getEloScore() + K * (0.5f - expectancyB);
        } else {
            //calculating score in case of win/lose
            score1 = winner.getEloScore() + K * (1 - expectancyA);
            score2 = loser.getEloScore() - K * expectancyB;
        }

        winner.setEloScore(Math.round(score1));
        loser.setEloScore(Math.round(score2));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("User with id %s not found", userId)));
    }
}
