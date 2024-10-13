package de.legend.LG_Backend.dtos.FightHistory;

public record FightHistoryResponseDto(
        long fightHistoryId,
        int pointsAttacker,
        int pointsOpponent,
        String battleMessage,
        FightLogDto[] logs

) {
}
