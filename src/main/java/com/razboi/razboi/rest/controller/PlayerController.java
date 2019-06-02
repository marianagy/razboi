package com.razboi.razboi.rest.controller;

import com.razboi.razboi.business.service.PlayerService;
import com.razboi.razboi.persistence.game.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class PlayerController {

    PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(value = "/players",
            method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<List<Player>> findAll() {
        return ResponseEntity.ok().body(playerService.findAll());
    }

    @RequestMapping(value = "/players/{id}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<Player> getById(@PathVariable int id) throws Exception {
        Player player = playerService.findById(id);
        if (player == null) {
            throw new RuntimeException("Player with id: "+ id +" not found");
        }
        return ResponseEntity.ok().body(player);
    }

    @RequestMapping(value = "/save-player",
            method = RequestMethod.POST
    )
    @ResponseBody
    public ResponseEntity save(@RequestBody Player player) {

        try {

            playerService.save(player);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(player);
    }



    @RequestMapping(value = "/update-player",
            method = RequestMethod.PUT

    )
    @ResponseBody
    public ResponseEntity update(@RequestBody Player player) {


        try {

            playerService.save(player);
        } catch (Exception e) {

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(player);
    }

    @RequestMapping(value = "/delete-player/{id}",
            method = RequestMethod.POST
    )
    @ResponseBody
    public ResponseEntity delete(@PathVariable int id) throws Exception {

        Player player = playerService.findById(id);

        // throw exception if null
        if (player == null) {
            return ResponseEntity.badRequest().build();
        }

        playerService.deleteById(id);

        return ResponseEntity.ok().build();
    }

}
