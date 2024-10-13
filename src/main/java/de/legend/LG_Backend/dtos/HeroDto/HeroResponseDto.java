package de.legend.LG_Backend.dtos.HeroDto;

public record HeroResponseDto(
        long id,
        String name,
        int powerLevel,
        String heroType,
        boolean isTaken
) {
}
