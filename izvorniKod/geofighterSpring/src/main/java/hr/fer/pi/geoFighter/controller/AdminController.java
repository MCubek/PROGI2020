package hr.fer.pi.geoFighter.controller;

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
    public ResponseEntity<List<String>> getCartographerApplications() {
        return new ResponseEntity<>(adminService.getCartographerApplications(), OK);
    }

    @PostMapping("/cartographerApplications/accept/{username}")
    public ResponseEntity<String> acceptCartographerApplication(@PathVariable String username) {
        adminService.acceptCartographerApplication(username);
        return new ResponseEntity<>("Cartographer application accepted", OK);
    }
}
