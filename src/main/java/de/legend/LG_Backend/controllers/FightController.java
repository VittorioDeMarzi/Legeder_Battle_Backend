package de.legend.LG_Backend.controllers;

import de.legend.LG_Backend.servicies.FightService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/legender_battle/fight")
public class FightController {

    final FightService fightService;

    public FightController(FightService fightService) {
        this.fightService = fightService;
    }

    @PostMapping
    public ResponseEntity<Void> startFight(@RequestParam Long userId, Authentication authentication) {
        fightService.startFight(authentication, userId);
        return ResponseEntity.ok().build();
    }
}
