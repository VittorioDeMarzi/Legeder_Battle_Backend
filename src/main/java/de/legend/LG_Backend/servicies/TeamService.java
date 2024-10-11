package de.legend.LG_Backend.servicies;

import de.legend.LG_Backend.dtos.TeamDtos.TeamRequestDto;
import de.legend.LG_Backend.dtos.TeamDtos.TeamResponseDto;
import de.legend.LG_Backend.entities.Team;
import de.legend.LG_Backend.entities.User;
import de.legend.LG_Backend.repository.TeamRepository;
import de.legend.LG_Backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
public class TeamService {

    final UserRepository userRepository;
    final TeamRepository teamRepository;

    public TeamService(UserRepository userRepository, TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public void addNewTeam(TeamRequestDto dto, Authentication authentication){
        User user = getUser(authentication);
        Team team = new Team(dto.teamName());
        teamRepository.save(team);
    }

    public void updateTeamName(TeamRequestDto dto, Authentication authentication){
        User user = getUser(authentication);
        Team team = user.getTeam();
        team.setTeamName(dto.teamName());
        teamRepository.save(team);
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
        Team team = user.getTeam();
        return new TeamResponseDto(team.getTeamName(), team.getWins(), team.getLoses());
    }
}
