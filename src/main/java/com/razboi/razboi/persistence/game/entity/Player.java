package com.razboi.razboi.persistence.game.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

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

    @Column(name="position")
    private String position;

    public Player() {

    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
