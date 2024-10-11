package de.legend.LG_Backend.repository;

import de.legend.LG_Backend.entities.Team;
import de.legend.LG_Backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

}
