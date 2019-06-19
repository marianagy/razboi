package com.razboi.razboi.persistence.game;

import com.razboi.razboi.persistence.game.entity.Game;
import com.razboi.razboi.persistence.game.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerDAO extends JpaRepository<Player, Integer> {


    @Query("Select p.participatedInGame from Player p where p.username=?1")
    List<Game> getAllGamesForAPlayer(String username);

    @Query("select p from Player p where p.username=?1 and p.participatedInGame.id=?2")
    Player getPlayerByGame(String username, Integer gameId);
}
