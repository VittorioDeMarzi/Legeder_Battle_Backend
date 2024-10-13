package de.legend.LG_Backend.servicies;

import de.legend.LG_Backend.dtos.HeroDto.HeroResponseDto;
import de.legend.LG_Backend.dtos.TeamDtos.TeamRequestDto;
import de.legend.LG_Backend.dtos.TeamDtos.TeamResponseDto;
import de.legend.LG_Backend.entities.Hero;
import de.legend.LG_Backend.entities.Team;
import de.legend.LG_Backend.entities.User;
import de.legend.LG_Backend.repository.TeamRepository;
import de.legend.LG_Backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class TeamService {

    final UserRepository userRepository;
    final TeamRepository teamRepository;

    public TeamService(UserRepository userRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }


    public TeamResponseDto addNewTeam(TeamRequestDto dto, Authentication authentication) {
        User user = getUser(authentication);
        Team team = new Team(dto.teamName());
        team.setUser(user);
        user.setTeam(team);
        TeamResponseDto responseDto = new TeamResponseDto(team.getId(), team.getTeamName(), team.getWins(), team.getLoses());
        teamRepository.save(team);
        userRepository.save(user);
        return responseDto;
    }

    public void updateTeamName(TeamRequestDto dto, Authentication authentication){
        User user = getUser(authentication);
        try {
            Team team = user.getTeam();
            team.setTeamName(dto.teamName());
            teamRepository.save(team);
        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(Authentication authentication){
        return userRepository.findByEmail(authentication.getName()).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    public void deleteTeam(Authentication authentication){
        User user = getUser(authentication);
        Team team = user.getTeam();
        team.setTeamName("");
        team.setLoses(0);
        team.setWins(0);
        teamRepository.save(team);
    }

    public TeamResponseDto getTeamData(Authentication authentication){
        User user = getUser(authentication);
        try {
            Team team = user.getTeam();
            return new TeamResponseDto(team.getId(), team.getTeamName(), team.getWins(), team.getLoses());
        } catch (Exception e) {
            throw new NullPointerException();
        }
    }

    public List<HeroResponseDto> getFightTeam(Authentication authentication){
        User user = getUser(authentication);
        Team team = user.getTeam();

        if(team ==null) throw new NoSuchElementException("User does not have a team");
        List<Hero> heroList = team.getTakenHeroes();
        return heroList.stream()
                .map(hero -> new HeroResponseDto(
                        hero.getId(),
                        hero.getName(),
                        hero.getPowerLevel(),
                        hero.getHeroType().getName(),
                        hero.isTaken()))
                .toList();
    }

//    public List<TeamResponseDto> getAllTeamWithoutOwnTeam(Authentication authentication){
//        long userId = getUser(authentication).getId();
//        List<Team> teamList = teamRepository.findAll();
//        teamList.stream().filter(team -> {})
//        try {
//            Team team = user.getTeam();
//            return new TeamResponseDto(team.getId(), team.getTeamName(), team.getWins(), team.getLoses());
//        } catch (Exception e) {
//            throw new NullPointerException();
//        }
//    }

}
