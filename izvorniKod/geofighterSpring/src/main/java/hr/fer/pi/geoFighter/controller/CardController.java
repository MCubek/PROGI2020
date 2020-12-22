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
    public List<CardDTO> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/cardCollection")
    public ResponseEntity<List<CardDTO>> getCardCollection() {
        return new ResponseEntity<>(cardService.getCardCollection(), OK);
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

    //PRIJAVA KARTE
    @PostMapping("/applyCard")
    public ResponseEntity<String> applyCard(@RequestBody CardDTO cardDTO) {
        cardService.applyLocationCard(cardDTO);
        return new ResponseEntity<>("Card applied for review", OK);
    }

}
