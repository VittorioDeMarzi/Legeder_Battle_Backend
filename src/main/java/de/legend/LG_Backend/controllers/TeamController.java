package de.legend.LG_Backend.controllers;

import de.legend.LG_Backend.dtos.TeamDtos.TeamRequestDto;
import de.legend.LG_Backend.dtos.TeamDtos.TeamResponseDto;
import de.legend.LG_Backend.dtos.userDtos.UserIdDto;
import de.legend.LG_Backend.servicies.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/legender_battle/team")
public class TeamController {

    final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping()
    public ResponseEntity<Void> newTeam(@RequestBody @Validated TeamRequestDto dto){
        teamService.addNewTeam(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public TeamResponseDto getTeamData(@RequestBody @Validated UserIdDto dto){
        try {
            return teamService.getTeamData(dto.userId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping
    public void deleteTeam(@RequestBody @Validated UserIdDto dto){
        try {
            teamService.deleteTeam(dto.userId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping
    public void updateTeamName(TeamRequestDto dto){
        try {
            teamService.updateTeamName(dto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
