package de.legend.LG_Backend.dtos.TeamDtos;

public record TeamWithUserIdResponseDto(
        long userId,
        Long teamId,
        String teamName,
        int wins,
        int loses
) {
}
