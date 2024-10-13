package de.legend.LG_Backend.servicies;

import de.legend.LG_Backend.dtos.HeroDto.FightTeamRequestDto;
import de.legend.LG_Backend.dtos.HeroDto.HeroIdDto;
import de.legend.LG_Backend.dtos.HeroDto.HeroRequestDto;
import de.legend.LG_Backend.dtos.HeroDto.HeroResponseDto;
import de.legend.LG_Backend.entities.Hero;
import de.legend.LG_Backend.entities.HeroType;
import de.legend.LG_Backend.entities.Team;
import de.legend.LG_Backend.entities.User;
import de.legend.LG_Backend.repository.HeroRepository;
import de.legend.LG_Backend.repository.HeroTypeRepository;
import de.legend.LG_Backend.repository.TeamRepository;
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
    final TeamRepository teamRepository;

    public HeroService(HeroRepository heroRepository, UserRepository userRepository, HeroTypeRepository heroTypeRepository, TeamRepository teamRepository) {
        this.heroRepository = heroRepository;
        this.userRepository = userRepository;
        this.heroTypeRepository = heroTypeRepository;
        this.teamRepository = teamRepository;
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
        return new HeroResponseDto(hero.getId(), hero.getName(), hero.getPowerLevel(), hero.getHeroType().getName(), hero.isTaken());
    }

    public int randomLevelPower(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public List<HeroResponseDto> getAllHeroes(Authentication authentication) {
        Team team = getTeam(authentication);
        List<Hero> heroes = heroRepository.findAllByTeam(team);
        return getHeroResponseDto(heroes);
    }


    public HeroResponseDto getHero(long heroId, Authentication authentication) {
        Team team = getTeam(authentication);
        Hero hero = heroRepository.findByTeamAndId(team, heroId).orElseThrow(() -> new NoSuchElementException("Hero not found"));
        return new HeroResponseDto(hero.getId(), hero.getName(), hero.getPowerLevel(), hero.getHeroType().getName(), hero.isTaken());
    }

    public void setTaken(HeroIdDto dto, Authentication authentication) {
        Team team = getTeam(authentication);
        Hero hero = heroRepository.findByTeamAndId(team, dto.heroId()).orElseThrow(() -> new NoSuchElementException("Hero not found"));
        hero.setTaken(!hero.isTaken());
        heroRepository.save(hero);
    }

    public List<HeroResponseDto> getAllOfOneHeroType(Long heroTypeId, Authentication authentication) {
        Team team = getTeam(authentication);
        List<Hero> heroes = heroRepository.findAllByHeroTypeIdAndTeam(heroTypeId, team);
        return getHeroResponseDto(heroes);
    }

    public List<HeroResponseDto> getHeroResponseDto(List<Hero> heroes) {
        return heroes.stream()
                .map(hero -> new HeroResponseDto(
                        hero.getId(),
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

    public void setFightTeam(FightTeamRequestDto dto, Authentication authentication) {
        long teamId = getTeam(authentication).getId();

        heroRepository.findAllByTeamId(teamId).forEach(hero -> {
            hero.setTaken(false);
            heroRepository.save(hero);
        });

        List<Long> heroIds = List.of(dto.hero1Id(), dto.hero2Id(), dto.hero3Id(), dto.hero4Id(), dto.hero5Id());

        heroIds.forEach(heroId -> {
            Hero hero = heroRepository.findById(heroId)
                    .orElseThrow(() -> new NoSuchElementException("Hero with ID " + heroId + " not found"));

            hero.setTaken(true);
            System.out.println("Set hero to taken: " + hero.getName());
            heroRepository.save(hero);
        });

        //prüfen ob das FightTeam vollständig ist
        isFightTeamComplete(authentication);

    }

    public void isFightTeamComplete(Authentication authentication) {
        Team team = getTeam(authentication);
        team.setPublic(checkFightTeamIsTaken(team));
        System.out.println(checkFightTeamIsTaken(team));
        teamRepository.save(team);
    }

    public boolean checkFightTeamIsTaken(Team team) {
        List<Hero> heroes = heroRepository.findAllByTeam(team);
        long rookies = heroes.stream().filter(hero -> hero.getHeroType().getId() == 1).count();
        long normals = heroes.stream().filter(hero -> hero.getHeroType().getId() == 2).count();
        long veterans = heroes.stream().filter(hero -> hero.getHeroType().getId() == 3).count();
        long legends = heroes.stream().filter(hero -> hero.getHeroType().getId() == 4).count();

        return (rookies == 1 && normals == 2 && veterans == 1 && legends == 1);



    }

}

