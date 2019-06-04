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


    @Column(name="cards")
    private String cards;


    @Column(name="wonCards")
    private String wonCards;



    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "wonGameID")
    private Game wonGame;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "participatedGameID")
    private Game participatedInGame;

    public Player() {

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

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

    public String getWonCards() {
        return wonCards;
    }

    public void setWonCards(String wonCards) {
        this.wonCards = wonCards;
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
