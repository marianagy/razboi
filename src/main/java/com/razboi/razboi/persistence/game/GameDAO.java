package com.razboi.razboi.persistence.game;

import com.razboi.razboi.persistence.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameDAO extends JpaRepository<Game, Integer> {


}
