package de.legend.LG_Backend.entities;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @Value("0")
    private int wins;

    @Column
    @Value("0")
    private int loses;

    @Column
    @Value("false")
    private boolean isPublic;

    @OneToOne
    private User user;

    @OneToMany
    private List<Hero> heroes;
    
    @Transient
    private int heroListSize;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<Hero> heroes) {
        this.heroes = heroes;
    }

    public int getHeroListSize() {
        return heroes.size();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
