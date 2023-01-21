package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/rest")
public class AdminController {
    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public ResponseEntity<?> getAllPlayers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String race,
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel,
            @RequestParam(required = false) String order,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "3") Integer pageSize) {

        try {
            List<Player> list = playerService.getAll(name, title, race, profession, after, before, banned,
                    minExperience, maxExperience, minLevel, maxLevel, order, pageNumber, pageSize);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/players/count")
    public ResponseEntity<?> getPlayersCount(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String race,
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel) {

        try {
            long count = playerService.count(name, title, race, profession, after, before, banned,
                    minExperience, maxExperience, minLevel, maxLevel);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<?> getPlayer(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(playerService.get(id), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/players")
    public ResponseEntity<?> createPlayer(@RequestBody Player player) {
        try {
            return new ResponseEntity<>(playerService.create(player), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<?> updatePlayer(@PathVariable Long id, @RequestBody Player player) {
        try {
            return new ResponseEntity<>(playerService.update(id, player), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable Long id) {
        try {
            playerService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
