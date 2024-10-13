package de.legend.LG_Backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class FightHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String battleName;

    @ManyToOne
    private User opponent;

    @Column
    private int attackerPoints;

    @Column
    private int opponentPoints;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    @OrderBy("timestamp ASC")
    private List<FightLog> fightLogs;

    public FightHistory() {
    }

    public FightHistory(String battleName, User attacker, User opponent, int attackerPoints, int opponentPoints) {
        this.battleName = battleName;
        this.attacker = attacker;
        this.opponent = opponent;
        this.attackerPoints = attackerPoints;
        this.opponentPoints = opponentPoints;
    }

    @Column
    private LocalDateTime timestamp;

    @PrePersist
    public void setTimestamp(){
        this.timestamp = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBattleName() {
        return battleName;
    }

    public void setBattleName(String battleName) {
        this.battleName = battleName;
    }

    public User getAttacker() {
        return attacker;
    }

    public void setAttacker(User attacker) {
        this.attacker = attacker;
    }

    public User getOpponent() {
        return opponent;
    }

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }

    public int getAttackerPoints() {
        return attackerPoints;
    }

    public void setAttackerPoints(int attackerPoints) {
        this.attackerPoints = attackerPoints;
    }

    public int getOpponentPoints() {
        return opponentPoints;
    }

    public void setOpponentPoints(int opponentPoints) {
        this.opponentPoints = opponentPoints;
    }

    public List<FightLog> getFightLogs() {
        return fightLogs;
    }

    public void setFightLogs(List<FightLog> fightLogs) {
        this.fightLogs = fightLogs;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
