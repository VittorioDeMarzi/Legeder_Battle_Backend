package de.legend.LG_Backend.servicies;

import de.legend.LG_Backend.repository.FightHistoryRepository;
import org.springframework.stereotype.Service;

@Service
public class FightHistoryService {

    final FightHistoryRepository fightHistoryRepository;

    public FightHistoryService(FightHistoryRepository fightHistoryRepository) {
        this.fightHistoryRepository = fightHistoryRepository;
    }


}
