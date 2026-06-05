package com.autotests.lichessbackend.controller;

import com.autotests.lichessbackend.dto.AddPlayerDto;
import com.autotests.lichessbackend.dto.PlayerResponseDto;
import com.autotests.lichessbackend.dto.PlayerStatsDto;
import com.autotests.lichessbackend.dto.SyncResponseDto;
import com.autotests.lichessbackend.entity.Player;
import com.autotests.lichessbackend.service.PlayerService;
import com.autotests.lichessbackend.service.StatsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
        log.info("REST sync request for player: {}", username);
        return playerService.syncPlayerGames(username);
    }

    @GetMapping("/{username}")
    public PlayerResponseDto getPlayer(@PathVariable String username) {
        log.trace("REST get player by username: {}", username);
        return playerService.getPlayer(username);
    }

    @GetMapping("/{username}/stats")
    public PlayerStatsDto getStats(@PathVariable String username) {
        log.trace("REST get stats for player: {}", username);
        return statsService.getStats(username);
    }

    @GetMapping
    public Page<Player> getAllPlayers(Pageable pageable) {
        log.trace("REST get all players with pageable: {}", pageable);
        return playerService.getAllPlayers(pageable);
    }

    @GetMapping("/id/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        log.trace("REST get player by id: {}", id);
        return playerService.getPlayerById(id);
    }

    @PostMapping
    public Player addPlayer(@Valid @RequestBody AddPlayerDto dto) {
        log.info("REST add player request: {}", dto.getUsername());
        return playerService.addPlayer(dto);
    }
}