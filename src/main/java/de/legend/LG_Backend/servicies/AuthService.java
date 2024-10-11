package de.legend.LG_Backend.servicies;

import de.legend.LG_Backend.dtos.userDtos.newUserRequestDto;
import de.legend.LG_Backend.entities.User;
import de.legend.LG_Backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    UserRepository userRepository;
    TokenService tokenService;
    PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(newUserRequestDto dto) {
        User user = new User(
                dto.email(),
                passwordEncoder.encode(dto.password()));
        return userRepository.save(user);
    }

    public String getJwt(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }
}
