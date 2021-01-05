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

    @GetMapping("/allCards")
    public ResponseEntity<List<CardDTO>> getAllCards() {
        return new ResponseEntity<>(cardService.getAllCards(), OK);
    }

    @GetMapping("/{locationCardId}")
    public ResponseEntity<CardDTO> getLocationCard(@PathVariable long locationCardId) {
        cardService.getLocationCard(locationCardId);
        return new ResponseEntity<>(cardService.getLocationCard(locationCardId), OK);
    }

    @DeleteMapping("/{locationCardId}")
    public ResponseEntity<Void> deleteLocationCard(@PathVariable long locationCardId) {
        cardService.deleteLocationCard(locationCardId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/edit")
    public ResponseEntity<String> editCardApplication(@RequestBody CardDTO card) {
        cardService.editLocatioCard(card);
        return new ResponseEntity<>("Card application edited", OK);
    }

    //PRIJAVA KARTE
    @PostMapping("/applyCard")
    public ResponseEntity<String> applyCard(@RequestBody CardDTO cardDTO) {
        cardService.applyLocationCard(cardDTO);
        return new ResponseEntity<>("Card applied for review", OK);
    }

    @GetMapping("/getNearby")
    public ResponseEntity<List<CardDTO>> getNearbyCards() {
        return new ResponseEntity<>(cardService.getNearbyCards(), OK);
    }

    @PostMapping("/collect")
    public ResponseEntity<Void> collectCard(@RequestBody Long cardId) {
        cardService.collectLocationCard(cardId);
        return new ResponseEntity<>(OK);
    }
}
