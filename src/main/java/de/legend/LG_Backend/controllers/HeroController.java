package de.legend.LG_Backend.controllers;

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

    @GetMapping
    public ResponseEntity<HeroResponseDto> getHero(@RequestBody @Validated HeroIdDto dto){
        return ResponseEntity.ok(heroService.getHero(dto));
    }

    @PostMapping("/taken")
    public ResponseEntity<Void> setTaken(@RequestBody @Validated HeroIdDto dto){
        heroService.setTaken(dto);
        return ResponseEntity.ok().build();
    }
}
