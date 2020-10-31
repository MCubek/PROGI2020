package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.RegisterRequest;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.model.VerificationToken;
import hr.fer.pi.geoFighter.repository.UserRepository;
import hr.fer.pi.geoFighter.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.geolatte.geom.V;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEnabled(false);
        //TODO Photo
        userRepository.save(user);

        String token = generateVerificationToken(user);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }
}
