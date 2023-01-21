package com.game.service;

import com.game.entity.Player;

import java.util.List;
import java.util.NoSuchElementException;

public interface PlayerService {
    Player get(long id) throws IllegalArgumentException, NoSuchElementException;
    List<Player> getAll(String name, String title, String race, String profession, Long after, Long before,
                        Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
                        Integer maxLevel, String order, Integer pageNumber, Integer pageSize) throws IllegalArgumentException;
    int count(String name, String title, String race, String profession, Long after, Long before,
              Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
              Integer maxLevel) throws IllegalArgumentException;
    Player create(Player player) throws IllegalArgumentException;
    Player update(long id, Player newPlayer) throws IllegalArgumentException, NoSuchElementException;
    void delete(long id) throws IllegalArgumentException, NoSuchElementException;
}
