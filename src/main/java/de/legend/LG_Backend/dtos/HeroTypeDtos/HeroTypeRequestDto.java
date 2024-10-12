package de.legend.LG_Backend.dtos.HeroTypeDtos;

import jakarta.validation.constraints.NotNull;

public record HeroTypeRequestDto(
        @NotNull
        Long heroTypeId
) {
}
