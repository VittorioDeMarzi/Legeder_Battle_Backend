package de.legend.LG_Backend.repository;

import de.legend.LG_Backend.entities.FightHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FightHistoryRepository extends JpaRepository<FightHistory, Long> {
}
