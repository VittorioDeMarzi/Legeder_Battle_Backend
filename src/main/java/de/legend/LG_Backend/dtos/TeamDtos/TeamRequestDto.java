package de.legend.LG_Backend.dtos.TeamDtos;

import jakarta.validation.constraints.NotBlank;

public record TeamRequestDto(
        @NotBlank(message = "Team name cannot be empty")
        String teamName
) {
}
