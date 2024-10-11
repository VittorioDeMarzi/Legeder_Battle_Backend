package de.legend.LG_Backend.dtos.userDtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserIdDto(
        @NotNull(message = "Id cannot be null")
        @Min(value = 1, message = "Id must be at least 1")
        Long user_id
) {
}
