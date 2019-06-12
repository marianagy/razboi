package com.razboi.razboi.business.service;

import com.razboi.razboi.persistence.game.entity.Game;
import com.razboi.razboi.persistence.game.entity.Player;

import java.util.List;

public interface PlayerService {

    List<Player> findAll();

    Player findById(int id) throws Exception;

    void save(Player player);

    void deleteById(int id);

    List<Game> findAllGamesByPlayer(String username);
}
