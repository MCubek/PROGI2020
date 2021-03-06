package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.*;
import hr.fer.pi.geoFighter.exceptions.SpringGeoFighterException;
import hr.fer.pi.geoFighter.exceptions.UserInfoInvalidException;
import hr.fer.pi.geoFighter.model.CartographerStatus;
import hr.fer.pi.geoFighter.model.NotificationEmail;
import hr.fer.pi.geoFighter.model.User;
import hr.fer.pi.geoFighter.model.VerificationToken;
import hr.fer.pi.geoFighter.repository.RoleRepository;
import hr.fer.pi.geoFighter.repository.UserRepository;
import hr.fer.pi.geoFighter.repository.VerificationTokenRepository;
import hr.fer.pi.geoFighter.security.JwtProvider;
import hr.fer.pi.geoFighter.util.ImageValidateUtility;
import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.IBANValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class AuthService {

    private final UrlBuilder urlBuilder;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshingTokenService;
    private final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
    private final IBANValidator ibanValidator = new IBANValidator();

    public void signup(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent() ||
                userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserInfoInvalidException("Username or email already used.");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(roleRepository.findByName("ROLE_USER").orElseThrow(() -> new SpringGeoFighterException("USER_ROLE not in database")));
        user.setEnabled(false);

        if (! urlValidator.isValid(registerRequest.getPhotoURL()))
            throw new UserInfoInvalidException("Invalid photo URL");

        URL url;
        try {
            url = new URL(registerRequest.getPhotoURL());
        } catch (MalformedURLException e) {
            throw new SpringGeoFighterException("Image URL error");
        }

        ImageValidateUtility.validateImage(url);

        user.setPhotoURL(url);

        userRepository.save(user);

        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail("Please activate your account.", user.getEmail(),
                urlBuilder.appendHost("api/auth/accountVerification/" + token)));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + username));
    }

    public void cartographerApply(CartographerRegisterRequest registerRequest) {
        User user = getCurrentUser();

        if (! ibanValidator.isValid(registerRequest.getIban()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid IBAN");

        if (! urlValidator.isValid(registerRequest.getIdPhotoURL()))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid photo URL");

        URL url;
        try {
            url = new URL(registerRequest.getIdPhotoURL());
        } catch (MalformedURLException e) {
            throw new SpringGeoFighterException("Image URL error");
        }

        ImageValidateUtility.validateImage(url);

        user.setCartographerStatus(CartographerStatus.APPLIED);
        user.setIban(registerRequest.getIban());
        user.setIdCardPhotoURL(url);
        userRepository.save(user);

    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid token."));
        fetchUserAndEnable(verificationToken.get());
        verificationTokenRepository.delete(verificationToken.get());
    }

    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringGeoFighterException("User " + username + " not found."));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate;
        try {
            authenticate = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (LockedException e) {
            User currentUser = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new SpringGeoFighterException("should never throw this"));
            String timeoutEnd = currentUser.getForcedTimeoutEnd().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

            throw new DisabledException("User disabled until " + timeoutEnd);
        }
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        return AuthenticationResponse.builder()
                .authorizationToken(token)
                .refreshToken(refreshingTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .role(getCurrentUser().getRole().getName())
                .build();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshingTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());

        return AuthenticationResponse.builder()
                .authorizationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .role(userRepository.findByUsername(refreshTokenRequest.getUsername()).get().getRole().getName())
                .build();
    }

}
