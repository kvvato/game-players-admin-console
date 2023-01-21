package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface CustomRepository extends JpaRepository<Player, Long> {
    @Query("SELECT p FROM Player p WHERE" +
            " (?1 IS NULL OR p.name LIKE %?1%)" +
            "  AND (?2 IS NULL OR p.title LIKE %?2%)" +
            "  AND (?3 IS NULL OR p.race = ?3)" +
            "  AND (?4 IS NULL OR p.profession = ?4)" +
            "  AND (?5 IS NULL OR p.birthday >= ?5)" +
            "  AND (?6 IS NULL OR p.birthday <= ?6)" +
            "  AND (?7 IS NULL OR p.banned = ?7)" +
            "  AND (?8 IS NULL OR p.experience >= ?8)" +
            "  AND (?9 IS NULL OR p.experience <= ?9)" +
            "  AND (?10 IS NULL OR p.level >= ?10)" +
            "  AND (?11 IS NULL OR p.level <= ?11)")
    Page<Player> findAllByParams(String name, String title, String race, String profession, Date after, Date before,
                                 Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel,
                                 Integer maxLevel, Pageable pageable);
    @Query("UPDATE Player SET" +
            " name = CASE WHEN ?2 IS NULL THEN name ELSE ?2 END," +
            " title = CASE WHEN ?3 IS NULL THEN title ELSE ?3 END," +
            " race = CASE WHEN ?4 IS NULL THEN race ELSE ?4 END," +
            " profession = CASE WHEN ?5 IS NULL THEN profession ELSE ?5 END," +
            " birthday = CASE WHEN ?6 IS NULL THEN birthday ELSE ?6 END," +
            " banned = CASE WHEN ?7 IS NULL THEN banned ELSE ?7 END," +
            " experience = CASE WHEN ?8 IS NULL THEN experience ELSE ?8 END" +
            " WHERE id = ?1")
    void update(long id, String name, String title, String race, String profession, Date birthday,
                  Boolean banned, Integer experience);
}
