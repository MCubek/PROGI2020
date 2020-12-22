package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.dto.SendRequestDTO;
import hr.fer.pi.geoFighter.dto.UserEloDTO;
import hr.fer.pi.geoFighter.dto.UserLocationDTO;
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

    @PostMapping("/storeLocation")
    public ResponseEntity<String> storeLocation(@RequestBody UserLocationDTO userLocationDTO){
        userService.storeLocation(userLocationDTO);
        return ResponseEntity.status(OK).body("Location saved!");
    }

    @GetMapping("/nearbyUsers{username}")
    public ResponseEntity<List<String>> getNearbyUsers(@PathVariable String username) {
        return new ResponseEntity<>(userService.getNearbyUsers(username), OK);
    }

    @PostMapping("/sendRequest")
    public ResponseEntity<String> sendRequest(@RequestBody SendRequestDTO sendRequestDTO){
        userService.sendRequest(sendRequestDTO);
        return ResponseEntity.status(OK).body("Request sent!");
    }

    @GetMapping("/getRequests{username}")
    public ResponseEntity<List<String>> getRequests(@PathVariable String username){
        return new ResponseEntity<>(userService.getRequests(username),OK);
    }

    @PostMapping("/receiveAnswer")
    public ResponseEntity<String> receiveAnswer(@RequestBody SendRequestDTO sendRequestDTO){
        userService.processAnswer(sendRequestDTO);
        return ResponseEntity.status(OK).body("Answer processed!");
    }

    @GetMapping("/getMatches{username}")
    public ResponseEntity<Boolean> getMatches(@PathVariable String username){
        return new ResponseEntity<>(userService.getMatches(username),OK);
    }

}
