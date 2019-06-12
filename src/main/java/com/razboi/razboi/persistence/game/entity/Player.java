package com.razboi.razboi.persistence.game.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "player")
public class Player implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer ID;


    @Column(name="username")
    private String username;



    @OneToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "wonGameID")
    private Game wonGame;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "participatedGameID")
    private Game participatedInGame;

    @Column(name = "points")
    private Integer points;

    @Transient
    private String currentChoice;

    public String getCurrentChoice() {
        return currentChoice;
    }

    public void setCurrentChoice(String currentChoice) {
        this.currentChoice = currentChoice;
    }

    public Player() {

    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public Game getWonGame() {
        return wonGame;
    }

    public void setWonGame(Game wonGame) {
        this.wonGame = wonGame;
    }

    public Game getParticipatedInGame() {
        return participatedInGame;
    }

    public void setParticipatedInGame(Game participatedInGame) {
        this.participatedInGame = participatedInGame;
    }
}
