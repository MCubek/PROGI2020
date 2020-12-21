package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.dto.CartographerUserDTO;
import hr.fer.pi.geoFighter.dto.DisableUserDTO;
import hr.fer.pi.geoFighter.service.AdminService;
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

    @GetMapping("/cartographerApplications")
    public ResponseEntity<List<CartographerUserDTO>> getCartographerApplications() {
        return new ResponseEntity<>(adminService.getCartographerApplications(), OK);
    }

    @PutMapping("/cartographerApplications/accept/{username}")
    public ResponseEntity<String> acceptCartographerApplication(@PathVariable String username) {
        adminService.acceptCartographerApplication(username);
        return new ResponseEntity<>("Cartographer application accepted", OK);
    }

    @GetMapping("/userList")
    public ResponseEntity<List<String>> getEnabledUsernames() {
        return new ResponseEntity<>(adminService.getEnabledUsernames(), OK);
    }

    @PutMapping("/promoteToAdmin/{username}")
    public ResponseEntity<String> promoteToAdmin(@PathVariable String username) {
        adminService.promoteToAdmin(username);
        return new ResponseEntity<>("User promoted to admin", OK);
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

}
