package de.legend.LG_Backend.security;

import de.legend.LG_Backend.repository.UserRepository;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.NoSuchElementException;

@Configuration
@EnableConfigurationProperties(RsaKeyProperties.class)
public class AppConfiguration {

    UserRepository userRepository;

    public AppConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    UserDetailsService userDetails() {
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
