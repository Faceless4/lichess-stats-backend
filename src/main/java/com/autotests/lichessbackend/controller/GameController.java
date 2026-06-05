package com.autotests.lichessbackend.controller;

import com.autotests.lichessbackend.dto.AddGameDto;
import com.autotests.lichessbackend.entity.Game;
import com.autotests.lichessbackend.service.GameService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public Page<Game> getAllGames(@PageableDefault(size = 5) Pageable pageable) {
        log.trace("REST get all games with pageable: {}", pageable);
        return gameService.getAllGames(pageable);
    }

    @GetMapping("/{id}")
    public Game getGameById(@PathVariable Long id) {
        log.trace("REST get game by id: {}", id);
        return gameService.getGameById(id);
    }

    @PostMapping
    public Game addGame(@Valid @RequestBody AddGameDto dto) {
        log.info("REST add game request. Lichess game id: {}", dto.getLichessGameId());
        return gameService.addGame(dto);
    }
}