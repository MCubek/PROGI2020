package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.dto.CardApplicationDTO;
import hr.fer.pi.geoFighter.dto.CardLocationDTO;
import hr.fer.pi.geoFighter.model.LocationCard;
import hr.fer.pi.geoFighter.service.CardApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/cartographer")
@AllArgsConstructor
public class CartographerController {

    private final CardApplicationService cardApplicationService;

    @GetMapping("/all")
    public ResponseEntity<List<CardApplicationDTO>> getAllCardApplications() {
        return new ResponseEntity<>(cardApplicationService.getAllCardApplications(), OK);
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<String> acceptCardApplication(@PathVariable Long id) {
        cardApplicationService.acceptCardApplication(id);
        return new ResponseEntity<>("Card application accepted", OK);
    }

    @PutMapping("/decline/{id}")
    public ResponseEntity<String> declineCardApplication(@PathVariable Long id) {
        cardApplicationService.declineCardApplication(id);
        return new ResponseEntity<>("Card application declined", OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editCardApplication(@RequestBody LocationCard card) {
        cardApplicationService.editCardApplication(card);
        return new ResponseEntity<>("Card application edited", OK);
    }

    @PutMapping("/confirm/{id}")
    public ResponseEntity<String> requestCardApplicationConfirmation(@PathVariable Long id) {
        cardApplicationService.requestCardApplicationConfirmation(id);
        return new ResponseEntity<>("Requested confirmation for card application", OK);
    }

    @GetMapping("/checked")
    public ResponseEntity<List<CardLocationDTO>> getAllToBeCheckedCoordinates() {
        return new ResponseEntity<>(cardApplicationService.getAllCardsThatNeedToBeChecked(), OK);
    }
}
