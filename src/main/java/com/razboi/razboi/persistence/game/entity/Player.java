package com.razboi.razboi.persistence.game.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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

    @Column(name = "chosenWord")
    private String chosenWord;

    @Transient
    private String chosenWordTransformed;

    public String getChosenWordTransformed() {
        return chosenWordTransformed;
    }

    public void setChosenWordTransformed(String chosenWordTransformed) {
        this.chosenWordTransformed = chosenWordTransformed;
    }

    @Column(name = "choices")
    private String choices;
    @Transient
    @JsonIgnore
    private String currentChoice;

    public String getChosenWord() {
        return chosenWord;
    }

    public void setChosenWord(String chosenWord) {
        this.chosenWord = chosenWord;
    }

    public String getChoices() {
        return choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(ID, player.ID) &&
                Objects.equals(username, player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, username);
    }
}
