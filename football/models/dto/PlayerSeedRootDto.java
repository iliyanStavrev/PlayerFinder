package com.example.football.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "players")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerSeedRootDto {

    List<PlayerSeedDto> player;

    public List<PlayerSeedDto> getPlayer() {
        return player;
    }

    public void setPlayer(List<PlayerSeedDto> player) {
        this.player = player;
    }
}
