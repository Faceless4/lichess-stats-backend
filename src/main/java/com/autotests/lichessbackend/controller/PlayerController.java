package com.autotests.lichessbackend.controller;



import com.autotests.lichessbackend.dto.PlayerResponseDto;
import com.autotests.lichessbackend.dto.PlayerStatsDto;
import com.autotests.lichessbackend.dto.SyncResponseDto;
import com.autotests.lichessbackend.service.PlayerService;
import com.autotests.lichessbackend.service.StatsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private final PlayerService playerService;
    private final StatsService statsService;

    public PlayerController(PlayerService playerService, StatsService statsService) {
        this.playerService = playerService;
        this.statsService = statsService;
    }

    @PostMapping("/{username}/sync")
    public SyncResponseDto sync(@PathVariable String username) {
        return playerService.syncPlayerGames(username);
    }

    @GetMapping("/{username}")
    public PlayerResponseDto getPlayer(@PathVariable String username) {
        return playerService.getPlayer(username);
    }

    @GetMapping("/{username}/stats")
    public PlayerStatsDto getStats(@PathVariable String username) {
        return statsService.getStats(username);
    }
}