package com.example.football.service.impl;

import com.example.football.models.dto.TownSeedDto;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;


@Service
public class TownServiceImpl implements TownService {

    private static final String FILE_PATH = "D:\\Работен плот\\JAVA DB SPRING\\EXAM_PREPARATION\\Java DB Spring Data Exam - 01 August 2021\\Player Finder_Skeleton\\skeleton\\skeleton\\src\\main\\resources\\files\\json\\towns.json";

    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;

    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper,
                           Gson gson, ValidationUtil validationUtil) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {

        return Files
                .readString(Path.of(FILE_PATH));
    }

    @Override
    public String importTowns() throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        TownSeedDto[] townSeedDtos =
                gson.fromJson(readTownsFileContent(), TownSeedDto[].class);

        Arrays.stream(townSeedDtos)
                .filter(townSeedDto -> {
                    boolean isValid = validationUtil.isValid(townSeedDto);

                    stringBuilder.append(isValid
                            ? String.format("Successfully imported Town %s - %d",
                            townSeedDto.getName(), townSeedDto.getPopulation())
                            : "Invalid Town")
                            .append(System.lineSeparator());

                    return isValid;
                }).map(townSeedDto -> modelMapper
                        .map(townSeedDto, Town.class))
                .forEach(townRepository::save);


        return stringBuilder.toString();
    }
}
