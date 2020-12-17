package hr.fer.pi.geoFighter.service;

import hr.fer.pi.geoFighter.dto.UserEloDTO;
import hr.fer.pi.geoFighter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@org.springframework.transaction.annotation.Transactional
public class UserService {

    private final UserRepository userRepository;

    public List<UserEloDTO> getUserEloInfo() {
        return new ArrayList<>(userRepository.getUserEloInfo());
    }
}
