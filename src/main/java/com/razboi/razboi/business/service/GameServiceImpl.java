package com.razboi.razboi.business.service;

import com.razboi.razboi.persistence.game.GameDAO;
import com.razboi.razboi.persistence.game.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    private GameDAO gameDAO;

    @Autowired
    public GameServiceImpl(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    @Override
    public List<Game> findAll() {
        return gameDAO.findAll();
    }

    @Override
    public Game findById(int id) throws Exception {
        Optional<Game> result = gameDAO.findById(id);
        if (result.isPresent()) {
            return result.get();

        } else{

            throw new RuntimeException("Game not found");

        }
    }

    @Override
    public void save(Game game) {
        gameDAO.save(game);
    }

    @Override
    public void deleteById(int id) {
        gameDAO.deleteById(id);
    }
}
