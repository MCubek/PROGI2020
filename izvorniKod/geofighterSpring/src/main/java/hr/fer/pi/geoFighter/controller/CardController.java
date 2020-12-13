package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.dto.CardDTO;
import hr.fer.pi.geoFighter.service.CardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/card")
@AllArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/cardCollection/{username}")
    public ResponseEntity<List<CardDTO>> getCardCollection(@PathVariable String username) {
        return new ResponseEntity<>(cardService.getCardCollection(username), OK);
    }

    @GetMapping("/{locationCardId}")
    public ResponseEntity<CardDTO> getLocationCard(@PathVariable long locationCardId) {
        cardService.getLocationCard(locationCardId);
        return new ResponseEntity<>(cardService.getLocationCard(locationCardId), OK);
    }

    //PRIJAVA KARTE
    @PutMapping("/{username}/apply/{name}")
    public ResponseEntity<String> applyNewLocationCard(@PathVariable String username, @PathVariable String name) {
        cardService.applyNewLocationCard(username, name);
        return new ResponseEntity<>("Card applied for review", OK);
    }

}
