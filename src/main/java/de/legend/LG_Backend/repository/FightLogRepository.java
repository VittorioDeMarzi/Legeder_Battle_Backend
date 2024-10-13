package de.legend.LG_Backend.repository;

import de.legend.LG_Backend.entities.FightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FightLogRepository extends JpaRepository<FightLog, Long> {
}
