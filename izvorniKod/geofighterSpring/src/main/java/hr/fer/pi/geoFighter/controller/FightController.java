package hr.fer.pi.geoFighter.controller;

import hr.fer.pi.geoFighter.dto.*;
import hr.fer.pi.geoFighter.service.FightService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.net.MalformedURLException;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/fight")
@AllArgsConstructor

public class FightController {

    private final FightService fightService;

    @GetMapping("/userCardList/{username}")
    public ResponseEntity<List<UserCardDTO>> getUserCardList(@PathVariable String username) throws MalformedURLException {
        return new ResponseEntity<>(fightService.getUserCardList(username), OK);
    }
}
