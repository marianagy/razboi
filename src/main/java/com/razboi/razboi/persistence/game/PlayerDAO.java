package com.razboi.razboi.persistence.game;

import com.razboi.razboi.persistence.game.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerDAO extends JpaRepository<Player, Integer> {

    //@Query("Select p from Player p where p.wonGame is not null")
    Player findByWonGame_IDIsNotNull();
}
