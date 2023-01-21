package com.game.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long id;
    public String name;
    public String title;
    @Enumerated(EnumType.STRING)
    public Race race;
    @Enumerated(EnumType.STRING)
    public Profession profession;
    public Date birthday;
    public Boolean banned = false;
    public Integer experience;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Integer level;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Integer untilNextLevel;

    public Player() {
    }

    public Player(Long id, String name, String title, Race race, Profession profession, Date birthday, Boolean banned, Integer experience, Integer level, Integer untilNextLevel) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.banned = banned;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
    }

    public void calculateLevel() {
        if (experience != null)
            level = (int) (Math.sqrt(2500 + 200 * experience) - 50) / 100;
    }

    public void calculateUntilNextLevel() {
        if (level != null && experience != null)
            untilNextLevel = 50 * (level + 1) * (level + 2) - experience;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race=" + race +
                ", profession=" + profession +
                ", birthday=" + birthday +
                ", banned=" + banned +
                ", experience=" + experience +
                ", level=" + level +
                ", untilNextLevel=" + untilNextLevel +
                '}';
    }
}
