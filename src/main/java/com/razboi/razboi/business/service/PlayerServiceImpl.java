package com.razboi.razboi.business.service;

import com.razboi.razboi.persistence.game.PlayerDAO;
import com.razboi.razboi.persistence.game.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    private PlayerDAO playerDAO;

    @Autowired
    public PlayerServiceImpl(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Override
    public List<Player> findAll() {
        return playerDAO.findAll();
    }

    @Override
    public Player findById(int id) throws Exception {
        Optional<Player> result = playerDAO.findById(id);
        if (result.isPresent()) {
            return result.get();

        } else{

            throw new RuntimeException("Player not found");

        }
    }

    @Override
    public void save(Player player) {
        playerDAO.save(player);
    }

    @Override
    public void deleteById(int id) {
        playerDAO.deleteById(id);
    }

}
