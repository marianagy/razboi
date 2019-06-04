package com.razboi.razboi.persistence.game;

import com.razboi.razboi.persistence.game.entity.Game;
import com.razboi.razboi.persistence.game.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameDAO extends JpaRepository<Game, Integer> {

    @Query("Select p FROM Player p where p.participatedInGame=?1")
    List<Player> getGame(Game game);
}
