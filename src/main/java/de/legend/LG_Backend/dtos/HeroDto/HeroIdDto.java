package de.legend.LG_Backend.dtos.HeroDto;

import jakarta.validation.constraints.NotNull;

public record HeroIdDto(
        @NotNull
        Long heroID
) {
}
