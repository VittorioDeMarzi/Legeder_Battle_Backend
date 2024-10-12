package de.legend.LG_Backend.servicies;

import de.legend.LG_Backend.entities.Hero;
import de.legend.LG_Backend.entities.HeroType;
import de.legend.LG_Backend.repository.HeroRepository;
import de.legend.LG_Backend.repository.HeroTypeRepository;
import de.legend.LG_Backend.repository.TeamRepository;
import de.legend.LG_Backend.repository.UserRepository;
import jakarta.validation.constraints.Null;
import org.springframework.stereotype.Service;

@Service
public class BattleService {

    final HeroRepository heroRepository;
    final UserRepository userRepository;
    final HeroTypeRepository heroTypeRepository;
    final TeamRepository teamRepository;

    public BattleService(HeroRepository heroRepository, UserRepository userRepository, HeroTypeRepository heroTypeRepository, TeamRepository teamRepository) {
        this.heroRepository = heroRepository;
        this.userRepository = userRepository;
        this.heroTypeRepository = heroTypeRepository;
        this.teamRepository = teamRepository;
    }

    public boolean didHeroBlock(double blockFactor) {
        double randomNumber = Math.random();
        return blockFactor >= randomNumber;
    }

    public void fight(long heroId, long opponentId){
        
    }
}

