package de.legend.LG_Backend.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid username format")
        String email,
        @NotBlank(message = "Password cannot be empty")
        String password
) {
}
