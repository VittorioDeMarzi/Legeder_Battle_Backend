package de.legend.LG_Backend.repository;

import de.legend.LG_Backend.entities.Hero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeroRepository extends JpaRepository<Hero, Long> {
}
