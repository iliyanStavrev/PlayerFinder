package com.example.football.service.impl;

import com.example.football.models.dto.StatSeedRootDto;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class StatServiceImpl implements StatService {


    private static final String FILE_PATH = "D:\\Работен плот\\JAVA DB SPRING\\EXAM_PREPARATION\\" +
            "Java DB Spring Data Exam - 01 August 2021\\Player Finder_Skeleton\\skeleton\\skeleton\\" +
            "src\\main\\resources\\files\\xml\\stats.xml";


    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final StatRepository statRepository;

    public StatServiceImpl(ModelMapper modelMapper,
                           XmlParser xmlParser, ValidationUtil validationUtil, StatRepository statRepository) {
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.statRepository = statRepository;
    }


    @Override
    public boolean areImported() {
        return statRepository.count() > 0;
    }

    @Override
    public String readStatsFileContent() throws IOException {
        return Files
                .readString(Path.of(FILE_PATH));
    }

    @Override
    public String importStats() throws JAXBException, FileNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();

        StatSeedRootDto statSeedRootDto = xmlParser
                .fromFile(FILE_PATH, StatSeedRootDto.class);

        statSeedRootDto.getStat()
                .stream()
                .filter(statSeedDto -> {
                    boolean isValid = validationUtil.isValid(statSeedDto);

                    stringBuilder.append(isValid
                                    ? String.format("Successfully imported Stat %.2f - %.2f - %.2f",
                                    statSeedDto.getPassing(), statSeedDto.getShooting(),
                                    statSeedDto.getEndurance())
                                    : "Invalid stat")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(statSeedDto -> modelMapper
                        .map(statSeedDto, Stat.class))
                .forEach(statRepository::save);

        return stringBuilder.toString();
    }
}
