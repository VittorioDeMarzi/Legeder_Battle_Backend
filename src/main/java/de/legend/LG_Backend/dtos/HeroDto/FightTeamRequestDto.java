package de.legend.LG_Backend.dtos.HeroDto;

import java.util.List;

public record FightTeamRequestDto(
        long hero1Id,
        long hero2Id,
        long hero3Id,
        long hero4Id,
        long hero5Id
) {

}
