package de.legend.LG_Backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class FightLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private FightHistory fightHistory;

    @Column
    private String message;

    @ManyToOne
    private Hero attacker;

    @ManyToOne
    private Hero defender;

    @Column
    @OrderBy("timestamp ASC")
    private LocalDateTime timestamp;

    @PrePersist
    public void setTimestamp(){
        this.timestamp = LocalDateTime.now();
    }

    public FightLog() {
    }

    public FightLog(FightHistory fightHistory, String message, Hero attacker, Hero defender) {
        this.fightHistory = fightHistory;
        this.message = message;
        this.attacker = attacker;
        this.defender = defender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FightHistory getFightHistory() {
        return fightHistory;
    }

    public void setFightHistory(FightHistory fightHistory) {
        this.fightHistory = fightHistory;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Hero getAttacker() {
        return attacker;
    }

    public void setAttacker(Hero attacker) {
        this.attacker = attacker;
    }

    public Hero getDefender() {
        return defender;
    }

    public void setDefender(Hero defender) {
        this.defender = defender;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
