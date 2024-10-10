package de.legend.LG_Backend.servicies;

import de.legend.LG_Backend.dtos.UserDto;
import de.legend.LG_Backend.entities.User;
import de.legend.LG_Backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    UserRepository userRepository;
    TokenService tokenService;

    public AuthService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;

    }

    public UserDto signup(UserDto dto) {
        User user = new User(
                dto.email(),
                dto.password());

        userRepository.save(user);
        return new UserDto(user.getUsername(), user.getPassword());

    }

    public String getJwt(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }
}
