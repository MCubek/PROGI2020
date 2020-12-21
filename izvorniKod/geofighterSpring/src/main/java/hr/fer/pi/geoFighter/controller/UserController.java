package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.dto.UserEloDTO;
import hr.fer.pi.geoFighter.dto.UserLocationDTO;
import hr.fer.pi.geoFighter.service.AuthService;
import hr.fer.pi.geoFighter.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping("/leaderboard")
    public ResponseEntity<List<UserEloDTO>> getUserEloInfo() {
        return new ResponseEntity<>(userService.getUserEloInfo(), OK);
    }

    @PostMapping("/storeLocation")
    public ResponseEntity<String> storeLocation(@RequestBody UserLocationDTO userLocationDTO){
        authService.storeLocation(userLocationDTO);
        return ResponseEntity.status(OK).body("Location saved!");
    }

    @GetMapping("/nearbyUsers{username}")
    public ResponseEntity<List<String>> getNearbyUsers(@PathVariable String username) {
        return new ResponseEntity<>(userService.getNearbyUsers(username), OK);
    }
}
