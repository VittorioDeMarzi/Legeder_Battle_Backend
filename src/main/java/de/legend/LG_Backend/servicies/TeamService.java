package de.legend.LG_Backend.servicies;

import de.legend.LG_Backend.dtos.TeamDtos.TeamRequestDto;
import de.legend.LG_Backend.dtos.TeamDtos.TeamResponseDto;
import de.legend.LG_Backend.entities.Team;
import de.legend.LG_Backend.entities.User;
import de.legend.LG_Backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TeamService {

    final UserRepository userRepository;

    public TeamService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addNewTeam(TeamRequestDto dto, Authentication authentication){
        User user = getUser(authentication);
        Team team = new Team(dto.teamName());
        user.setTeam(team);
        userRepository.save(user);
    }

    public void updateTeamName(TeamRequestDto dto, Authentication authentication){
        User user = getUser(authentication);
        Team team = user.getTeam();
        team.setTeamName(dto.teamName());
        userRepository.save(user);
    }

    public User getUser(Authentication authentication){
        String userName = authentication.getName();
        return userRepository.findByEmail(userName).orElseThrow(()-> new NoSuchElementException("User not found"));
    }

    public void deleteTeam(Authentication authentication){
        User user = getUser(authentication);
        Team team = user.getTeam();
        team.setTeamName("");
        team.setLoses(0);
        team.setWins(0);
        userRepository.save(user);
    }

    public TeamResponseDto getTeamData(Authentication authentication){
        User user = getUser(authentication);
        Team team = user.getTeam();
        return new TeamResponseDto(team.getTeamName(), team.getWins(), team.getLoses());
    }
}
