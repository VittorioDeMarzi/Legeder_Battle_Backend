package de.legend.LG_Backend.servicies;

import de.legend.LG_Backend.dtos.FightHistory.FightHistoryResponseDto;
import de.legend.LG_Backend.entities.*;
import de.legend.LG_Backend.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FightService {


    final HeroRepository heroRepository;
    final FightHistoryRepository fightHistoryRepository;
    final FightLogRepository fightLogRepository;
    final UserRepository userRepository;
    final TeamRepository teamRepository;

    public FightService(HeroRepository heroRepository,
                        FightHistoryRepository fightHistoryRepository,
                        FightLogRepository fightLogRepository,
                        UserRepository userRepository,
                        TeamRepository teamRepository) {
        this.heroRepository = heroRepository;
        this.fightHistoryRepository = fightHistoryRepository;
        this.fightLogRepository = fightLogRepository;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public Team getTeam(User user) {
        Team team = user.getTeam();
        if (team == null) throw new NoSuchElementException("User does not have a team");
        return team;
    }

    @Transactional
    public FightHistoryResponseDto startFight(Authentication authentication, long opponentId) {
        FightHistoryResponseDto dto = null;
        User user1 = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        User user2 = userRepository.findById(opponentId).orElseThrow(() -> new NoSuchElementException("User not found"));
        Team teamUser1 = getTeam(user1);
        Team teamUser2 = getTeam(user2);

        if (teamUser1.isPublic() && teamUser2.isPublic()) {

            List<Hero> heroesTeam1 = teamUser1.getTakenHeroes();
            List<Hero> heroesTeam2 = teamUser2.getTakenHeroes();

            FightHistory fightHistory = new FightHistory();
            fightHistory.setAttacker(user1);
            fightHistory.setOpponent(user2);

            String fightIntro = "Team " + teamUser1.getTeamName() + " (Helden: " + heroesTeam1
                    .stream().map(Hero::getName).collect(Collectors.joining(", ")) + ") vs. " +
                    "Team " + teamUser2.getTeamName() + " (Helden: " + heroesTeam2
                    .stream().map(Hero::getName).collect(Collectors.joining(", ")) + ")";
            FightLog introLog = new FightLog(fightHistory, fightIntro, null, null);
            fightLogRepository.save(introLog);
            fightHistory.setAttacker(user1);
            fightHistory.setOpponent(user2);

            HashSet<Long> usedHeroes = new HashSet<>();

            heroesTeam1.forEach(hero -> {
                long attacker = hero.getId();
                long heroType = hero.getHeroType().getId();

                heroesTeam2.stream()
                        .filter(hero1 -> heroType == hero1.getHeroType().getId() && !usedHeroes.contains(hero1.getId()))
                        .findFirst()
                        .ifPresent(hero1 -> {
                            long opponent = hero1.getId();
                            fight(attacker, opponent, fightHistory);
                            usedHeroes.add(opponent);
                        });
            });

            int attackerPoints = fightHistory.getAttackerPoints();
            int opponentPoints = fightHistory.getOpponentPoints();

            String winnerTeam;

            if(attackerPoints>opponentPoints) {
                teamUser1.setWins(teamUser1.getWins() + 1);
                teamUser2.setLoses(teamUser2.getLoses()+1);
                winnerTeam = teamUser1.getTeamName();
            }
            else {
                teamUser2.setWins(teamUser2.getWins() + 1);
                teamUser1.setLoses(teamUser1.getLoses() + 1);
                winnerTeam = teamUser2.getTeamName();
            }

            teamUser1.setMatchCounter(teamUser1.getMatchCounter()+1);
            teamUser2.setMatchCounter(teamUser2.getMatchCounter()+1);
            teamRepository.save(teamUser1);
            teamRepository.save(teamUser2);
            fightHistory.setBattleName("Team: " + teamUser1.getTeamName() + " fordert Team: "
                    + teamUser2.getTeamName() + " heraus! Nach einem harten aber fairen Kampf hat Team: "
                    + winnerTeam+ " gewonnen!");
            dto = new FightHistoryResponseDto(fightHistory.getId(), fightHistory.getAttackerPoints(), fightHistory.getOpponentPoints(), fightHistory.getBattleName());
            fightHistoryRepository.save(fightHistory);

        }
        return dto;
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
        blockFactor = Math.pow(blockFactor, 2);
        return blockFactor >= randomNumber;
    }

    public double getRandomDamage(double min, double max) {
        Random random = new Random();
        double mean = (min + max) / 2;
        double stddev = (max - min) / 6;

        double randomDamage = random.nextGaussian() * stddev + mean;
        randomDamage = Math.max(min, Math.min(randomDamage, max));
        return Math.round(randomDamage * 100.0) / 100.0;
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

        logFight(fightHistory, "----------- " + hero1Name + " vs. " + hero2Name +" -----------", attacker, defender);

        while (healthHero1 > 0 && healthHero2 > 0) {
            round++;
            logFight(fightHistory, "Runde: " + round, attacker, defender);

            if (isHero1Turn) {
                attacker = hero1;
                defender = hero2;



                if (heroIsBlock(hero2.calculateBlockFactor())) {
                    logFight(fightHistory, "Der Angriff von " + hero1Name + " wurde geblockt", attacker, defender);
                } else {
                    double damage = getRandomDamage(minDamageHero1, maxDamageHero1);
                    double originalDamage = damage;
                    damage = getCriticalHitChance(damage);
                    if (damage > originalDamage)
                        logFight(fightHistory, hero2Name + " erleidet einen kritischen Schaden von " + damage, attacker, defender);
                    else
                        logFight(fightHistory, hero2Name + " erleidet einen Schaden von " + damage, attacker, defender);
                    healthHero2 -= damage;
                }
            } else {
                attacker = hero2;
                defender = hero1;
                if (heroIsBlock(hero1.calculateBlockFactor())) {
                    logFight(fightHistory, "Der Angriff von " + hero2Name + " wurde geblockt", attacker, defender);
                } else {
                    double damage = getRandomDamage(minDamageHero2, maxDamageHero2);
                    double originalDamage = damage;
                    damage = getCriticalHitChance(damage);
                    if (damage > originalDamage)
                        logFight(fightHistory, hero1Name + " erleidet einen kritischen Schaden von " + damage, attacker, defender);
                    else
                        logFight(fightHistory, hero1Name + " erleidet einen Schaden von " + damage, attacker, defender);
                    healthHero1 -= damage;
                }
            }

            isHero1Turn = !isHero1Turn;
        }

        if (healthHero1 <= 0) {
            logFight(fightHistory, hero1Name + " geht zu Boden und verliert den Fight! " + hero2Name + " hat gewonnen!", attacker, defender);
            hero1Win = 1;
        } else {
            logFight(fightHistory, hero2Name + " geht zu Boden und verliert den Fight! " + hero1Name + " hat gewonnen!", attacker, defender);
            hero2Win = 1;
        }

        logFight(fightHistory, "--------------- ENDE ------------", attacker, defender);

        fightHistory.setAttackerPoints(fightHistory.getAttackerPoints() + hero1Win);
        fightHistory.setOpponentPoints(fightHistory.getOpponentPoints() + hero2Win);
    }

    private void logFight(FightHistory fightHistory, String message, Hero attacker, Hero defender) {
        FightLog fightLog = new FightLog(fightHistory, message, attacker, defender);
        fightLogRepository.save(fightLog);
    }
}
