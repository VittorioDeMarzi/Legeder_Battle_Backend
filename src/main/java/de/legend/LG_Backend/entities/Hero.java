package de.legend.LG_Backend.entities;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

@Entity
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private int powerLevel;

    @Column()
    @Value("false")
    private boolean isTaken;

    @ManyToOne
    private HeroType heroType;

    @ManyToOne
    private Team team;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }

    public HeroType getHeroType() {
        return heroType;
    }

    public void setHeroType(HeroType heroType) {
        this.heroType = heroType;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public double calculateBlockFactor() {
        double blockQuote = this.heroType.getBlockQuote();
        double level = this.powerLevel;
        double blockFactor = (level / 100) + blockQuote;
        return Math.min(blockFactor, 1.0);
    }

    public double calculateMaxDamage() {
        double level = this.powerLevel;
        double damage = this.heroType.getDamage();
        return (level * this.heroType.getAttackFactor()) + damage;
    }
}
