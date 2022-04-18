package com.example.football.service.impl;

import com.example.football.models.dto.PlayerSeedRootDto;
import com.example.football.models.entity.Player;
import com.example.football.models.entity.Town;
import com.example.football.repository.PlayerRepository;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.PlayerService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {


    private static final String FILE_PATH = "D:\\Работен плот\\JAVA DB SPRING\\EXAM_PREPARATION\\" +
            "Java DB Spring Data Exam - 01 August 2021\\Player Finder_Skeleton\\skeleton\\skeleton\\src" +
            "\\main\\resources\\files\\xml\\players.xml";


    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final PlayerRepository playerRepository;
    private final TownRepository townRepository;
    private final StatRepository statRepository;
    private final TeamRepository teamRepository;

    public PlayerServiceImpl(ModelMapper modelMapper, XmlParser xmlParser,
                             ValidationUtil validationUtil,
                             PlayerRepository playerRepository, TownRepository townRepository,
                             StatRepository statRepository, TeamRepository teamRepository) {
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.playerRepository = playerRepository;
        this.townRepository = townRepository;
        this.statRepository = statRepository;
        this.teamRepository = teamRepository;
    }


    @Override
    public boolean areImported() {
        return playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files
                .readString(Path.of(FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {

        StringBuilder stringBuilder = new StringBuilder();

        PlayerSeedRootDto playerSeedRootDto = xmlParser
                .fromFile(FILE_PATH, PlayerSeedRootDto.class);

        playerSeedRootDto.getPlayer()
                .stream()
                .filter(playerSeedDto -> {
                    boolean isValid = validationUtil.isValid(playerSeedDto);

                    Optional<Player> optPlayer = playerRepository
                            .findByEmail(playerSeedDto.getEmail());

                    if (optPlayer.isPresent()){
                        stringBuilder.append("Invalid Player");
                        return false;
                    }

                        stringBuilder.append(isValid
                                        ? String.format("Successfully imported Player %s %s - %s",
                                        playerSeedDto.getFirstName(),
                                        playerSeedDto.getLastName(),
                                        playerSeedDto.getPosition())
                                        : "Invalid Player")
                                .append(System.lineSeparator());

                        return isValid;
                })
                .map(playerSeedDto -> {
                    Player player = modelMapper
                            .map(playerSeedDto, Player.class);

                    player.setTown(townRepository.findByName(playerSeedDto
                            .getTown().getName()));
                    player.setTeam(teamRepository.findTeamByName(playerSeedDto
                            .getTeam().getName()));
                    player.setStat(statRepository.findById(playerSeedDto
                            .getStat().getId()).orElse(null));

                    return player;
                })
                .forEach(playerRepository::save);

        return stringBuilder.toString();
    }

    @Override
    public String exportBestPlayers() {

        return playerRepository
                .findBestPlayers(LocalDate.of(1995, 1, 1),
                        LocalDate.of(2003, 1, 1))
                .stream()
                .map(player -> String.format("Player - %s %s\n" +
                        "\tPosition - %s\n" +
                        "\tTeam - %s\n" +
                        "\tStadium - %s\n",player.getFirstName(),
                        player.getLastName(),
                        player.getPosition(),
                        player.getTeam().getName(),
                        player.getTeam().getStadiumName()))
                .collect(Collectors.joining(""));

    }
}
