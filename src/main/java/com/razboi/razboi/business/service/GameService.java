package com.razboi.razboi.business.service;

import com.razboi.razboi.persistence.game.entity.Game;

import java.util.List;

public interface GameService {

    List<Game> findAll();

    Game findById(int id) throws Exception;

    void save(Game game);

    void deleteById(int id);
}
