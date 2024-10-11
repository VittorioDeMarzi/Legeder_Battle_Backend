package de.legend.LG_Backend.dtos.userDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record newUserRequestDto(
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid username format")
        String email,
        @NotBlank(message = "Password cannot be empty")
        String password
) {
}
