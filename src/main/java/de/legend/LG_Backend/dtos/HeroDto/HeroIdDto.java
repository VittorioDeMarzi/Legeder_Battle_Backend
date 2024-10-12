package de.legend.LG_Backend.dtos.HeroDto;

import jakarta.validation.constraints.Min;

public record HeroIdDto(
        @Min(value = 1)
        Long heroId
) {
}
