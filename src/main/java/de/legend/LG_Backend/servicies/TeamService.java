package de.legend.LG_Backend.servicies;

import de.legend.LG_Backend.dtos.TeamDtos.TeamRequestDto;
import de.legend.LG_Backend.entities.Team;
import de.legend.LG_Backend.entities.User;
import de.legend.LG_Backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class TeamService {

    final UserRepository userRepository;

    public TeamService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addNewTeam(TeamRequestDto dto){
        User user = getUserById(dto.user_id());
        Team team = new Team(dto.teamName());
        user.setTeam(team);
        userRepository.save(user);
    }

    public void updateTeamName(TeamRequestDto dto){
        User user = getUserById(dto.user_id());
        Team team = user.getTeam();
        team.setTeamName(dto.teamName());
        userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()-> new NoSuchElementException("User not found"));
    }

    public void deleteTeam(Long id){
        User user = getUserById(id);
        Team team = user.getTeam();
        team.setTeamName("");
        team.setLoses(0);
        team.setWins(0);
        userRepository.save(user);
    }
}
