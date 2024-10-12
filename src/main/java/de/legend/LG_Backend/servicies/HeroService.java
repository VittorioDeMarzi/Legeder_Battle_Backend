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

    public HeroResponseDto createNewHero(HeroRequestDto heroRequestDto, Authentication authentication) {
        Hero hero = new Hero();

        Team team = getTeam(authentication);

        HeroType heroType = heroTypeRepository.findById(heroRequestDto.heroTypeId()).orElseThrow(() -> new NoSuchElementException("HeroType not found"));
        hero.setHeroType(heroType);
        hero.setName(heroRequestDto.name());
        hero.setPowerLevel(randomLevelPower(heroType.getMinPower(), heroType.getMaxPower()));
        hero.setTeam(team);
        team.getHeroes().add(hero);
        heroRepository.save(hero);
        return new HeroResponseDto(hero.getName(), hero.getPowerLevel(), hero.getHeroType().getName(), hero.isTaken());
    }

    public int randomLevelPower(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public List<HeroResponseDto> getAllHeroes(Authentication authentication) {
        Team team = getTeam(authentication);
        List<Hero> heroes = heroRepository.findAllByTeam(team);

        return getHeroResponseDto(heroes);
    }


    public HeroResponseDto getHero(HeroIdDto dto, Authentication authentication) {
        Team team = getTeam(authentication);
        Hero hero = heroRepository.findByTeamAndId(team, dto.heroId()).orElseThrow(() -> new NoSuchElementException("Hero not found"));
        return new HeroResponseDto(hero.getName(), hero.getPowerLevel(), hero.getHeroType().getName(), hero.isTaken());
    }

    public void setTaken(HeroIdDto dto, Authentication authentication) {
        Team team = getTeam(authentication);
        Hero hero = heroRepository.findByTeamAndId(team, dto.heroId()).orElseThrow(() -> new NoSuchElementException("Hero not found"));
        hero.setTaken(!hero.isTaken());
        heroRepository.save(hero);
    }

    public List<HeroResponseDto> getAllOfOneHeroTypeAndNotTaken(Long heroTypeId, Authentication authentication) {
        Team team = getTeam(authentication);
        List<Hero> heroes = heroRepository.findAllByHeroTypeIdAndTeamAndIsTakenFalse(heroTypeId, team);
        return getHeroResponseDto(heroes);
    }

    private static List<HeroResponseDto> getHeroResponseDto(List<Hero> heroes) {
        return heroes.stream()
                .map(hero -> new HeroResponseDto(
                        hero.getName(),
                        hero.getPowerLevel(),
                        hero.getHeroType().getName(),
                        hero.isTaken()))
                .toList();
    }

    private Team getTeam(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Team team = user.getTeam();
        if (team == null) throw new NoSuchElementException("Team not founded");
        return team;
    }
}
