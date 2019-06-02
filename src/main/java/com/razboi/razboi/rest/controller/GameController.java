package com.razboi.razboi.rest.controller;

import com.razboi.razboi.business.service.GameService;
import com.razboi.razboi.persistence.game.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(value = "/games",
            method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<List<Game>> findAll() {
        return ResponseEntity.ok().body(gameService.findAll());
    }

    @RequestMapping(value = "/games/{id}",
            method = RequestMethod.GET
    )
    @ResponseBody
    public ResponseEntity<Game> getById(@PathVariable int id) throws Exception {
        Game game = gameService.findById(id);
        if (game == null) {
            throw new RuntimeException("Game with id: "+ id +" not found");
        }
        return ResponseEntity.ok().body(game);
    }

    @RequestMapping(value = "/save-game",
            method = RequestMethod.POST
    )
    @ResponseBody
    public ResponseEntity save(@RequestBody Game game) {

        try {

            gameService.save(game);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(game);
    }



    @RequestMapping(value = "/update-game",
            method = RequestMethod.PUT

    )
    @ResponseBody
    public ResponseEntity update(@RequestBody Game game) {


        try {

            gameService.save(game);
        } catch (Exception e) {

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(game);
    }

    @RequestMapping(value = "/delete-games/{id}",
            method = RequestMethod.POST
    )
    @ResponseBody
    public ResponseEntity delete(@PathVariable int id) throws Exception {

        Game game = gameService.findById(id);

        // throw exception if null
        if (game == null) {
            return ResponseEntity.badRequest().build();
        }

        gameService.deleteById(id);

        return ResponseEntity.ok().build();
    }

}
