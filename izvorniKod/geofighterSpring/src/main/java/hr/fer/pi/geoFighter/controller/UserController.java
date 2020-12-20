package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.dto.UserEloDTO;
import hr.fer.pi.geoFighter.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/leaderboard")
    public ResponseEntity<List<UserEloDTO>> getUserEloInfo() {
        return new ResponseEntity<>(userService.getUserEloInfo(), OK);
    }

    @GetMapping("/userList")
    public ResponseEntity<List<String>> getEnabledUsers() {
        return new ResponseEntity<>(userService.getEnabledUsers(), OK);
    }

    @GetMapping("/userProfile/{username}")
    public ResponseEntity<List<String>> userProfile(@PathVariable String username){
        return new ResponseEntity<>(userService.userProfile(username), OK);
    }
}
