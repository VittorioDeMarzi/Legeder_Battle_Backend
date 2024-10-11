package de.legend.LG_Backend.repository;

import de.legend.LG_Backend.entities.Hero;
import de.legend.LG_Backend.entities.Team;
import de.legend.LG_Backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeroRepository extends JpaRepository<Hero, Long> {
    List<Hero> findAllByTeam(Team team);
}
