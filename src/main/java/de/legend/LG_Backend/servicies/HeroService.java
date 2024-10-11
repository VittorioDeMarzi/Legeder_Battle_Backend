package de.legend.LG_Backend.servicies;

import de.legend.LG_Backend.dtos.HeroDto.HeroIdDto;
import de.legend.LG_Backend.dtos.HeroDto.HeroRequestDto;
import de.legend.LG_Backend.dtos.HeroDto.HeroResponseDto;
import de.legend.LG_Backend.entities.Hero;
import de.legend.LG_Backend.entities.HeroType;
import de.legend.LG_Backend.entities.Team;
import de.legend.LG_Backend.entities.User;
import de.legend.LG_Backend.repository.HeroRepository;
import de.legend.LG_Backend.repository.HeroTypeRepository;
import de.legend.LG_Backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HeroService {

    final HeroRepository heroRepository;
    final UserRepository userRepository;
    final HeroTypeRepository heroTypeRepository;

    public HeroService(HeroRepository heroRepository, UserRepository userRepository, HeroTypeRepository heroTypeRepository) {
        this.heroRepository = heroRepository;
        this.userRepository = userRepository;
        this.heroTypeRepository = heroTypeRepository;
    }

    public Hero createNewHero(HeroRequestDto heroRequestDto, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new NoSuchElementException("User not found"));
        Hero hero = new Hero();
        Team team = user.getTeam();
        HeroType heroType = heroTypeRepository.findById(heroRequestDto.heroTypeId()).orElseThrow(()-> new NoSuchElementException("HeroType not found"));
        hero.setHeroType(heroType);
        hero.setName(heroRequestDto.name());
        hero.setPowerLevel(randomLevelPower(heroType.getMinPower(), heroType.getMaxPower()));
        team.getHeroes().add(hero);
        return heroRepository.save(hero);
    }

    public int randomLevelPower(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public List<HeroResponseDto> getAllHeroes(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

//        List<Hero> heroes = heroRepository.findAllByUser(user);

//        return heroes.stream()
//                .map(hero -> new HeroResponseDto(
//                        hero.getName(),
//                        hero.getPowerLevel(),
//                        hero.getHeroType().getName()))
//                .toList();
    }

    public HeroResponseDto getHero(HeroIdDto dto){
        Hero hero  = heroRepository.findById(dto.heroID()).orElseThrow(()-> new NoSuchElementException("Hero not found"));
            return new HeroResponseDto(hero.getName(), hero.getPowerLevel(), hero.getHeroType().getName());
    }

}
