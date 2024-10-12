package de.legend.LG_Backend.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class HeroType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private int minPower;

    @Column
    private int maxPower;

    @Column
    private double blockQuote;

    @Column
    private double attackFactor;

    @Column
    private double health;

    @Transient
    private double damage;

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

    public int getMinPower() {
        return minPower;
    }

    public void setMinPower(int minPower) {
        this.minPower = minPower;
    }

    public int getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(int maxPower) {
        this.maxPower = maxPower;
    }

    public double getBlockQuote() {
        return blockQuote;
    }

    public void setBlockQuote(double blockQuote) {
        this.blockQuote = blockQuote;
    }

    public double getAttackFactor() {
        return attackFactor;
    }

    public void setAttackFactor(double attackFactor) {
        this.attackFactor = attackFactor;
    }

    public double getDamage() {
        if (minPower == 0 || maxPower == 0) return 0;
        return maxPower - minPower;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }
}
