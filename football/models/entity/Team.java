package com.example.football.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Team extends BaseEntity{

    private String name;
    private String stadiumName;
    private Integer fanBase;
    private String history;
    private Town town;

    @Column(unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "stadium_name")
    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }


    public Integer getFanBase() {
        return fanBase;
    }

    public void setFanBase(Integer fanBase) {
        this.fanBase = fanBase;
    }

    @Column(columnDefinition = "TEXT")
    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    @ManyToOne
    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }
}
