package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.repository.CustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerServiceImpl implements PlayerService {
    @Autowired
    private CustomRepository customRepository;

    @Override
    public Player get(long id) throws IllegalArgumentException, NoSuchElementException {
        if (id < 1)
            throw new IllegalArgumentException();
        Optional<Player> player = customRepository.findById(id);
        if (player.isPresent())
            return player.get();
        else
            throw new NoSuchElementException();
    }

    @Override
    public List<Player> getAll(String name, String title, String race, String profession, Long after, Long before,
                               Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
                               Integer maxLevel, String order, Integer pageNumber, Integer pageSize) throws IllegalArgumentException {
        if (pageNumber < 0 || pageSize < 0)
            throw new IllegalArgumentException();

        Pageable pageable;
        if (pageSize == 0) {
            pageable = Pageable.unpaged();
        } else {
            String orderFieldName = order == null ? "id" : PlayerOrder.valueOf(order).getFieldName();
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by(orderFieldName));
        }

        Date afterDate = after == null ? null : new Date(after);
        Date beforeDate = before == null ? null : new Date(before);

        Page<Player> page = customRepository.findAllByParams(
                name, title, race, profession, afterDate, beforeDate, banned,
                minExperience, maxExperience, minLevel, maxLevel, pageable);

        return page.getContent();
    }

    @Override
    public int count(String name, String title, String race, String profession, Long after, Long before,
                     Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
                     Integer maxLevel) throws IllegalArgumentException {
        List<Player> list = getAll(name, title, race, profession, after, before, banned, minExperience, maxExperience,
                minLevel, maxLevel, null, 0, 0);
        return list.size();
    }

    @Override
    public Player create(Player player) throws IllegalArgumentException {
        if (player.name == null || player.title == null || player.race == null || player.profession == null
                || player.birthday == null || player.experience == null || invalidPlayerInfo(player))
            throw new IllegalArgumentException();

        player.calculateLevel();
        player.calculateUntilNextLevel();
        return customRepository.save(player);
    }

    @Override
    public Player update(long id, Player newPlayer) throws IllegalArgumentException, NoSuchElementException {
        if (id < 1 || invalidPlayerInfo(newPlayer)
                || newPlayer.name == null && newPlayer.title == null && newPlayer.race == null && newPlayer.profession == null
                && newPlayer.birthday == null && newPlayer.banned == null && newPlayer.experience == null)
            throw new IllegalArgumentException();

        Optional<Player> oldPlayer = customRepository.findById(id);
        if (!oldPlayer.isPresent())
            throw new NoSuchElementException();

        Player player = oldPlayer.get();

        if (newPlayer.name != null && !player.name.equals(newPlayer.name))
            player.name = newPlayer.name;
        if (newPlayer.title != null && !player.title.equals(newPlayer.title))
            player.title = newPlayer.title;
        if (newPlayer.race != null && player.race != newPlayer.race)
            player.race = newPlayer.race;
        if (newPlayer.profession != null && player.profession != newPlayer.profession)
            player.profession = newPlayer.profession;
        if (newPlayer.birthday != null && !player.birthday.equals(newPlayer.birthday))
            player.birthday = newPlayer.birthday;
        if (newPlayer.banned != null && player.banned != newPlayer.banned)
            player.banned = newPlayer.banned;
        if (newPlayer.experience != null && player.experience != newPlayer.experience)
            player.experience = newPlayer.experience;

        player.calculateLevel();
        player.calculateUntilNextLevel();
        return customRepository.save(player);
    }

    @Override
    public void delete(long id) throws IllegalArgumentException, NoSuchElementException {
        if (id < 1) throw new IllegalArgumentException();
        try {
            customRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException();
        }
    }

    private boolean invalidPlayerInfo(Player player) {
        if (player.name != null && player.name.length() > 12 || player.title != null && player.title.length() > 30
                || player.experience != null && (player.experience < 0 || player.experience > 10000000))
            return true;

        if (player.birthday != null) {
            Date year2000 = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();
            Date year3000 = new GregorianCalendar(3000, Calendar.JANUARY, 1).getTime();
            return player.birthday.before(year2000) || player.birthday.after(year3000);
        }

        return false;
    }
}
