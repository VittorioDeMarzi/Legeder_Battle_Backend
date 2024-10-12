package de.legend.LG_Backend.entities;

import jakarta.persistence.*;

@Entity
public class BattleLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String battleName;

    @Column
    private User attacker;

    @Column User opponent;


}
