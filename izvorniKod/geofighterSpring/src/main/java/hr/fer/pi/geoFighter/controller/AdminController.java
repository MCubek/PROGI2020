package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.dto.*;
import hr.fer.pi.geoFighter.service.AdminService;
import hr.fer.pi.geoFighter.service.CardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final CardService cardService;

    @GetMapping("/cartographerApplications")
    public ResponseEntity<List<CartographerUserDTO>> getCartographerApplications() {
        return new ResponseEntity<>(adminService.getCartographerApplications(), OK);
    }

    @PutMapping("/cartographerApplications/accept/{username}")
    public ResponseEntity<String> acceptCartographerApplication(@PathVariable String username) {
        adminService.acceptCartographerApplication(username);
        return new ResponseEntity<>("Cartographer application accepted", OK);
    }

    @PutMapping("/cartographerApplications/decline/{username}")
    public ResponseEntity<String> declineCartographerApplication(@PathVariable String username) {
        adminService.declineCartographerApplication(username);
        return new ResponseEntity<>("Cartographer application declined", OK);
    }

    @GetMapping("/userList")
    public ResponseEntity<List<UserDTO>> getEnabledUsers() {
        return new ResponseEntity<>(adminService.getEnabledUsers(), OK);
    }

    @PutMapping("/promoteToAdmin/{username}")
    public ResponseEntity<String> promoteToAdmin(@PathVariable String username) {
        adminService.promoteToAdmin(username);
        return new ResponseEntity<>("User promoted to admin", OK);
    }

    @PutMapping("/demoteFromAdmin/{username}")
    public ResponseEntity<String> demoteFromAdmin(@PathVariable String username) {
        adminService.demoteFromAdmin(username);
        return new ResponseEntity<>("User demoted from admin", OK);
    }

    @PutMapping("/deleteUser/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        adminService.deleteUser(username);
        return new ResponseEntity<>("User deleted", OK);
    }

    @PutMapping("/disableUser")
    public ResponseEntity<String> disableUser(@RequestBody DisableUserDTO disableUserDTO) {
        adminService.disableUser(disableUserDTO);
        return new ResponseEntity<>("User disabled until "+disableUserDTO.getTimeoutEnd(), OK);
    }

    @GetMapping("/allCards")
    public ResponseEntity<List<CardDTO>> getCardCollection() {
        return new ResponseEntity<>(adminService.getCardCollection(), OK);
    }

    @PostMapping("/editCard")
    public ResponseEntity<String> editLocationCard(@RequestBody CardDTO card) {
        adminService.editLocationCard(card);
        return new ResponseEntity<>("Card application edited", OK);
    }

    @DeleteMapping("/{locationCardId}")
    public ResponseEntity<Void> deleteLocationCard(@PathVariable long locationCardId) {
        adminService.deleteLocationCard(locationCardId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/editUser")
    public ResponseEntity<String> editUser(@RequestBody UserDTO userDTO) {
        adminService.editUser(userDTO);
        return new ResponseEntity<>("User edited", OK);
    }

}
