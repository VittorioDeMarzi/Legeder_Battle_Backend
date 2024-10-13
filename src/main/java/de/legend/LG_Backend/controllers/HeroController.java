package de.legend.LG_Backend.controllers;

import de.legend.LG_Backend.dtos.HeroDto.FightTeamRequestDto;
import de.legend.LG_Backend.dtos.HeroDto.HeroIdDto;
import de.legend.LG_Backend.dtos.HeroDto.HeroRequestDto;
import de.legend.LG_Backend.dtos.HeroDto.HeroResponseDto;
import de.legend.LG_Backend.servicies.HeroService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/legender_battle/hero")
public class HeroController {

    final HeroService heroService;

    public HeroController(HeroService heroService) {
        this.heroService = heroService;
    }

    @PostMapping
    public ResponseEntity<HeroResponseDto> createNewHero(@RequestBody @Validated HeroRequestDto dto, Authentication authentication){
        return ResponseEntity.ok(heroService.createNewHero(dto, authentication));
    }

    @GetMapping("/list")
    public ResponseEntity<List<HeroResponseDto>> getAllHeroes(Authentication authentication){
        return ResponseEntity.ok(heroService.getAllHeroes(authentication));
    }

    @GetMapping("/getHero")
    public ResponseEntity<HeroResponseDto> getHero(@RequestParam Long heroId, Authentication authentication){
        return ResponseEntity.ok(heroService.getHero(heroId, authentication));
    }

    @PutMapping("/taken")
    public ResponseEntity<Void> setTaken(@RequestBody @Validated HeroIdDto dto, Authentication authentication){
        heroService.setTaken(dto, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getHeroList")
    public ResponseEntity<List<HeroResponseDto>> getHeroesFromOneHeroType(@RequestParam Long heroId, Authentication authentication){
        return ResponseEntity.ok(heroService.getAllOfOneHeroType(heroId, authentication));
    }

    @PutMapping("/setFightTeam")
    public ResponseEntity<Void> setFightTeam(@RequestBody @Validated FightTeamRequestDto dto, Authentication authentication){
        heroService.setFightTeam(dto, authentication);
        return ResponseEntity.ok().build();
    }
}
