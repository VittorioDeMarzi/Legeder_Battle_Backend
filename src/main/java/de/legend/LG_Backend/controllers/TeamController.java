package de.legend.LG_Backend.controllers;

import de.legend.LG_Backend.dtos.TeamDtos.TeamRequestDto;
import de.legend.LG_Backend.dtos.TeamDtos.TeamResponseDto;
import de.legend.LG_Backend.servicies.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("api/v1/legender_battle/team")
public class TeamController {

    final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping()
    public ResponseEntity<TeamResponseDto> newTeam(@RequestBody @Validated TeamRequestDto dto, Authentication authentication) {
        return ResponseEntity.ok(teamService.addNewTeam(dto, authentication));
    }

    @GetMapping()
    public ResponseEntity<TeamResponseDto> getTeamData(Authentication authentication) {
        try {
            TeamResponseDto teamData = teamService.getTeamData(authentication);
            return ResponseEntity.ok(teamData);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping
    public void deleteTeam(Authentication authentication) {
        try {
            teamService.deleteTeam(authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping
    public void updateTeamName(TeamRequestDto dto, Authentication authentication) {
        try {
            teamService.updateTeamName(dto, authentication);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @PutMapping
//    public void setTeamPublic(Authentication a)
}
