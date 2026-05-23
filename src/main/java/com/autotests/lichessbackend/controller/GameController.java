package com.autotests.lichessbackend.controller;

import org.springframework.data.web.PageableDefault;
import com.autotests.lichessbackend.entity.Game;
import com.autotests.lichessbackend.service.GameService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import com.autotests.lichessbackend.dto.AddGameDto;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public Page<Game> getAllGames(@PageableDefault(size = 5) Pageable pageable) {
        return gameService.getAllGames(pageable);
    }

    @GetMapping("/{id}")
    public Game getGameById(@PathVariable Long id) {
        return gameService.getGameById(id);
    }

    @PostMapping
    public Game addGame(@RequestBody AddGameDto dto) {
        return gameService.addGame(dto);
    }
}