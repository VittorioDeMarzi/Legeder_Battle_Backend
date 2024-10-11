package de.legend.LG_Backend.dtos.HeroDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HeroRequestDto(
        @NotBlank(message = "name cannot be blank")
        String name,
        @NotNull
        long heroTypeId
) {
}
