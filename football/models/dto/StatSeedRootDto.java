package com.example.football.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "stats")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatSeedRootDto {

    private List<StatSeedDto> stat;

    public List<StatSeedDto> getStat() {
        return stat;
    }

    public void setStat(List<StatSeedDto> stat) {
        this.stat = stat;
    }
}
