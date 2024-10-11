package de.legend.LG_Backend.controllers;

import de.legend.LG_Backend.dtos.JwtDto;
import de.legend.LG_Backend.dtos.UserDto;
import de.legend.LG_Backend.entities.User;
import de.legend.LG_Backend.servicies.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/legender_battle/auth")
public class AuthController {

    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signup (@RequestBody @Validated UserDto dto) {
        return ResponseEntity.ok(authService.signup(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login (Authentication authentication) {
        return ResponseEntity.ok(new JwtDto(authService.getJwt(authentication)));
    }
}
