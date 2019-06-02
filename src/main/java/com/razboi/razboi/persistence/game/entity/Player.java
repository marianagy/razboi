package com.razboi.razboi.persistence.game.entity;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer ID;


    @Column(name="username")
    private String username;

    @ElementCollection
    @CollectionTable(name="Cards", joinColumns=@JoinColumn(name="jucator_id"))
    @Column(name="cards")
    private List<String> cards;

    @ElementCollection
    @CollectionTable(name="WonCards", joinColumns=@JoinColumn(name="jucator_id"))
    @Column(name="wonCards")
    private List<String> wonCards;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wonGameID")
    private Game wonGame;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }

    public List<String> getWonCards() {
        return wonCards;
    }

    public void setWonCards(List<String> wonCards) {
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
