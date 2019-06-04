package com.razboi.razboi.business.service;

import com.razboi.razboi.persistence.game.entity.Player;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PlayerService {

    List<Player> findAll();

    Player findById(int id) throws Exception;

    void save(Player player);

    void deleteById(int id);

    Player winner();
}
