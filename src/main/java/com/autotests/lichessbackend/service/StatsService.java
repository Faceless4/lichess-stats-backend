package com.autotests.lichessbackend.service;



import com.autotests.lichessbackend.dto.PlayerStatsDto;
import com.autotests.lichessbackend.entity.Game;
import com.autotests.lichessbackend.entity.Player;
import com.autotests.lichessbackend.repository.GameRepository;
import com.autotests.lichessbackend.util.GameResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private final PlayerService playerService;
    private final GameRepository gameRepository;

    public StatsService(PlayerService playerService, GameRepository gameRepository) {
        this.playerService = playerService;
        this.gameRepository = gameRepository;
    }

    @Transactional(readOnly = true)
    public PlayerStatsDto getStats(String username) {
        Player player = playerService.getPlayerEntity(username);
        List<Game> games = gameRepository.findAllByPlayer(player);

        long wins = games.stream()
                .filter(game -> GameResult.WIN.equals(game.getResult()))
                .count();

        long losses = games.stream()
                .filter(game -> GameResult.LOSS.equals(game.getResult()))
                .count();

        long draws = games.stream()
                .filter(game -> GameResult.DRAW.equals(game.getResult()))
                .count();

        Map<String, Long> openings = games.stream()
                .collect(Collectors.groupingBy(
                        game -> game.getOpeningName() == null || game.getOpeningName().isBlank()
                                ? "Unknown opening"
                                : game.getOpeningName(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));

        return new PlayerStatsDto(
                player.getUsername(),
                games.size(),
                wins,
                losses,
                draws,
                openings
        );
    }
}