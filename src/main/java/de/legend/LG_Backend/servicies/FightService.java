package de.legend.LG_Backend.servicies;

import de.legend.LG_Backend.entities.*;
import de.legend.LG_Backend.repository.FightHistoryRepository;
import de.legend.LG_Backend.repository.FightLogRepository;
import de.legend.LG_Backend.repository.HeroRepository;
import de.legend.LG_Backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class FightService {


    final HeroRepository heroRepository;
    final FightHistoryRepository fightHistoryRepository;
    final FightLogRepository fightLogRepository;
    final UserRepository userRepository;

    public FightService(HeroRepository heroRepository, FightHistoryRepository fightHistoryRepository, FightLogRepository fightLogRepository, UserRepository userRepository) {
        this.heroRepository = heroRepository;
        this.fightHistoryRepository = fightHistoryRepository;
        this.fightLogRepository = fightLogRepository;
        this.userRepository = userRepository;
    }

    public Team getTeam(User user){
        Team team = user.getTeam();
        if (team == null) throw new NoSuchElementException("User does not have a team");
        return team;
    }

    public void startFight(Authentication authentication, long opponentId) {
        User user1 = userRepository.findByEmail(authentication.getName())
                .orElseThrow(()-> new NoSuchElementException("User not found"));
        User user2 = userRepository.findById(opponentId).orElseThrow(()-> new NoSuchElementException("User not found"));
        Team teamUser1 = getTeam(user1);
        Team teamUser2 = getTeam(user2);

        FightHistory fightHistory = new FightHistory();
        fightHistory.setAttacker(user1);
        fightHistory.setOpponent(user2);

        long hero1Id = 1L;
        long hero2Id = 2L;
        fight(hero1Id, hero2Id, fightHistory);

        fightHistoryRepository.save(fightHistory);
    }


    // Logic for a match
    private static final double CRITICAL_HIT_CHANCE = 0.2;

    public static double getCriticalHitChance(double damage) {
        if (Math.random() < CRITICAL_HIT_CHANCE) {
            return damage * 1.5;
        }
        return damage;
    }

    public Hero getHero(long id) {
        return heroRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Hero Id:" + id + " not found"));
    }

    public boolean heroIsBlock(double blockFactor) {
        double randomNumber = Math.random();
        return blockFactor >= randomNumber;
    }

    public double getRandomDamage(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    public void fight(long heroId, long opponentId, FightHistory fightHistory) {
        Hero hero1 = getHero(heroId);
        Hero hero2 = getHero(opponentId);

        double maxDamageHero1 = hero1.calculateMaxDamage();
        double maxDamageHero2 = hero2.calculateMaxDamage();

        double minDamageHero1 = hero1.getHeroType().getDamage();
        double minDamageHero2 = hero2.getHeroType().getDamage();

        double healthHero1 = hero1.getHeroType().getHealth();
        double healthHero2 = hero2.getHeroType().getHealth();

        String hero1Name = hero1.getName();
        String hero2Name = hero2.getName();

        int hero1Win = 0;
        int hero2Win = 0;

        int round = 0;
        boolean isHero1Turn = Math.random() < 0.5;
        String logMessage;
        Hero attacker = null;
        Hero defender = null;

        while (healthHero1 > 0 && healthHero2 > 0) {
            round++;
            if (isHero1Turn) {
                attacker = hero1;
                defender = hero2;
                if (heroIsBlock(hero2.calculateBlockFactor())) {
                    logMessage = "Der Angriff von " + hero1Name + " wurde geblockt";
                } else {
                    double damage = getRandomDamage(minDamageHero1, maxDamageHero1);
                    damage = getCriticalHitChance(damage);
                    healthHero2 -= damage;
                    logMessage = hero2Name + "erleidet einen Schaden von " + damage;
                }
            } else {
                attacker = hero2;
                defender = hero1;
                if (heroIsBlock(hero1.calculateBlockFactor())) {
                    logMessage = "Der Angriff von " + hero2Name + " wurde geblockt";
                } else {
                    double damage = getRandomDamage(minDamageHero2, maxDamageHero2);
                    damage = getCriticalHitChance(damage);
                    healthHero1 -= damage;
                    logMessage = hero1Name + "erleidet einen Schaden von " + damage;
                }
            }

            FightLog fightLog = new FightLog(
                   fightHistory,
                   logMessage,
                   attacker,
                   defender
            );

            fightLogRepository.save(fightLog);
            isHero1Turn = !isHero1Turn;
        }

        if (healthHero1 <= 0) {
            logMessage = " geht zu Boden und verliert den Fight" + hero2Name + " hat gewonnen!";
            hero1Win = 1;
        }
        else {
            logMessage = " geht zu Boden und verliert den Fight" + hero1Name + " hat gewonnen!";
            hero2Win = 1;
        }

        FightLog fightLog = new FightLog(
                fightHistory,
                logMessage,
                attacker,
                defender
        );

        fightHistory.setAttackerPoints(hero1Win);
        fightHistory.setOpponentPoints(hero2Win);
    }
}
