package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.dto.FightDTO;
import hr.fer.pi.geoFighter.service.FightService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/fight")
@AllArgsConstructor
public class FightController {

    private final FightService fightService;

    @PostMapping("/startFight")
    public ResponseEntity<String> fight(@RequestBody FightDTO fightDTO) {
        return new ResponseEntity<>(fightService.fight(fightDTO), OK);
    }

}
