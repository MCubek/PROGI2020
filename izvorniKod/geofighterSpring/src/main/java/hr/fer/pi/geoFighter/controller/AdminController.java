package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static org.springframework.http.HttpStatus.OK;

import java.util.Collection;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/cartographerApplies")
    public Collection<String> getCartographerApplies() {
        return adminService.getCartographerApplies();
    }

    @PostMapping("/cartographerApplies/accept/{username}")
    public ResponseEntity<String> acceptCartographerApply(@PathVariable String username) {
        adminService.acceptCartographerApply(username);
        return new ResponseEntity("Cartographer apply accepted", OK);
    }
}
