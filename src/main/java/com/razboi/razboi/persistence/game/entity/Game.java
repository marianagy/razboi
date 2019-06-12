package com.razboi.razboi.persistence.game.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "game")
public class Game implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer ID;

    @Column(name = "words")
    String words;


    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "participatedInGame")
    List<Player> participants;

    @Transient
    private List<String> game_words;

    public List<String> getGame_words() {
        return game_words;
    }

    public void setGame_words(List<String> game_words) {
        this.game_words = game_words;
    }

    @OneToOne(cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        mappedBy = "wonGame")
    Player winner;

    public String getWords() {
        return words;
    }


    public void setWords(String words) {
        this.words = words;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Game() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public List<Player> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Player> participants) {
        this.participants = participants;
    }


}
