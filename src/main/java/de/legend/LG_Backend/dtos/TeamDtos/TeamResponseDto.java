package de.legend.LG_Backend.dtos.TeamDtos;

public record TeamResponseDto(
        Long id,
        String teamName,
        int wins,
        int loses
) {
}
