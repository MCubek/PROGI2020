package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.dto.*;
import hr.fer.pi.geoFighter.service.FightService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/fight")
@AllArgsConstructor
public class FightController {

    private final FightService fightService;

    @PutMapping("/submitCards")
    public ResponseEntity<Void> submitCards(@RequestBody List<SubmitCardFightDTO> fightCards) {
        fightService.submitCards(fightCards);
        return new ResponseEntity<>(OK);
    }

    @PutMapping("/startFight/{fightId}")
    public ResponseEntity<Boolean> startFight(@PathVariable Long fightId) {
        Boolean result = fightService.startFight(fightId);
        return new ResponseEntity<>(result, OK);
    }

    @GetMapping("/getWinner/{fightId}")
    public ResponseEntity<String> getWinner(@PathVariable Long fightId) {
        return new ResponseEntity<>(fightService.getWinnerOfFight(fightId), OK);
    }

    @GetMapping("/userCardList/{username}")
    public ResponseEntity<List<UserCardDTO>> getUserCardList(@PathVariable String username) {
        return new ResponseEntity<>(fightService.getUserCardList(username), OK);
    }

    @PostMapping("/sendRequest")
    public ResponseEntity<String> sendRequest(@RequestBody SendRequestDTO sendRequestDTO){
        fightService.sendRequest(sendRequestDTO);
        return ResponseEntity.status(OK).body("Request sent!");
    }

    @GetMapping("/getRequests{username}")
    public ResponseEntity<List<String>> getRequests(@PathVariable String username){
        return new ResponseEntity<>(fightService.getRequests(username),OK);
    }

    @PostMapping("/receiveAnswer")
    public ResponseEntity<String> receiveAnswer(@RequestBody SendRequestDTO sendRequestDTO){
        fightService.processAnswer(sendRequestDTO);
        return ResponseEntity.status(OK).body("Answer processed!");
    }

    @GetMapping("/getMatches{username}")
    public ResponseEntity<SendRequestDTO> getMatches(@PathVariable String username){
        return new ResponseEntity<>(fightService.getMatches(username),OK);
    }

    @DeleteMapping("/delete/{fightId}")
    public ResponseEntity<Void> deleteFight(@PathVariable Long fightId) {
        fightService.deleteFight(fightId);
        return new ResponseEntity<>(OK);
    }
}
