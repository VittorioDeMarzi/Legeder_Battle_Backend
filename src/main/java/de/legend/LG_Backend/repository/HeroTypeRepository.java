package de.legend.LG_Backend.repository;

import de.legend.LG_Backend.entities.HeroType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroTypeRepository extends JpaRepository<HeroType, Long> {
}
