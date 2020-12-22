package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.dto.*;
import hr.fer.pi.geoFighter.service.FightService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/fight")
@AllArgsConstructor
public class FightController {

    private final FightService fightService;

    @PutMapping("/submitCards")
    public ResponseEntity<Void> submitCards(@RequestBody Long[] fightCards) {
        fightService.submitCards(fightCards);
        return new ResponseEntity<>(OK);
    }

    @PutMapping("/startFight")
    public ResponseEntity<Void> startFight() {
        fightService.startFight();
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/getWinner/{fightId}")
    public ResponseEntity<String> getWinner(@PathVariable Long fightId) {
        return new ResponseEntity<>(fightService.getWinnerOfFight(fightId), OK);
    }

    @GetMapping("/userCardList/{username}")
    public ResponseEntity<List<UserCardDTO>> getUserCardList(@PathVariable String username) throws MalformedURLException {
        return new ResponseEntity<>(fightService.getUserCardList(username), OK);
    }

    @DeleteMapping("/delete/{fightId}")
    public ResponseEntity<Void> deleteFight(@PathVariable Long fightId) {
        fightService.deleteFight(fightId);
        return new ResponseEntity<>(OK);
    }
}
