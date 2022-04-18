package com.example.football.service.impl;

import com.example.football.models.dto.TeamSeedDto;
import com.example.football.models.entity.Team;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TeamServiceImpl implements TeamService {

    private static final String FILE_PATH = "D:\\Работен плот\\JAVA DB SPRING\\EXAM_PREPARATION\\" +
            "Java DB Spring Data Exam - 01 August 2021\\Player Finder_Skeleton\\skeleton\\skeleton" +
            "\\src\\main\\resources\\files\\json\\teams.json";

    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final TownRepository townRepository;

    public TeamServiceImpl(TeamRepository teamRepository, ModelMapper modelMapper,
                           Gson gson, ValidationUtil validationUtil, TownRepository townRepository) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files
                .readString(Path.of(FILE_PATH));
    }

    @Override
    public String importTeams() throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        TeamSeedDto[] teamSeedDtos =
                gson.fromJson(readTeamsFileContent(), TeamSeedDto[].class);

        Arrays.stream(teamSeedDtos)
                .filter(teamSeedDto -> {
                    boolean isValid = validationUtil.isValid(teamSeedDto);

                    stringBuilder.append(isValid
                                    ? String.format("Successfully imported Team %s - %d",
                                    teamSeedDto.getName(), teamSeedDto.getFanBase())
                                    : "Invalid Team")
                            .append(System.lineSeparator());

                    return isValid;
                }).map(teamSeedDto -> {
                 Team team = modelMapper
                            .map(teamSeedDto, Team.class);

                 team.setTown(townRepository.findByName(teamSeedDto.getTownName()));

                 return team;
                })
                .forEach(teamRepository::save);


        return stringBuilder.toString();
    }
}
