package com.razboi.razboi.persistence.game;

import com.razboi.razboi.persistence.game.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerDAO extends JpaRepository<Player, Integer> {
}
