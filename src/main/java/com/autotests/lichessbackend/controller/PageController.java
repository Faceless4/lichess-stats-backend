package com.autotests.lichessbackend.controller;

import com.autotests.lichessbackend.dto.AddPlayerDto;
import com.autotests.lichessbackend.service.PlayerService;
import com.autotests.lichessbackend.service.StatsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

    private final PlayerService playerService;
    private final StatsService statsService;

    public PageController(PlayerService playerService, StatsService statsService) {
        this.playerService = playerService;
        this.statsService = statsService;
    }

    @GetMapping("/players-page")
    public String playersPage(Model model, Pageable pageable) {
        model.addAttribute("players", playerService.getAllPlayers(pageable));
        model.addAttribute("playerForm", new AddPlayerDto());
        return "players";
    }

    @PostMapping("/players-page")
    public String addPlayer(
            @Valid @ModelAttribute("playerForm") AddPlayerDto playerForm,
            BindingResult bindingResult,
            Model model,
            Pageable pageable
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("players", playerService.getAllPlayers(pageable));
            model.addAttribute("playerForm", playerForm);
            return "players";
        }

        playerService.addPlayer(playerForm);
        return "redirect:/players-page";
    }

    @PostMapping("/players-page/{username}/sync")
    public String syncPlayer(@PathVariable String username) {
        playerService.syncPlayerGames(username);
        return "redirect:/players-page";
    }

    @GetMapping("/players-page/{username}/stats")
    public String playerStats(@PathVariable String username, Model model) {
        model.addAttribute("stats", statsService.getStats(username));
        return "player-stats";
    }
}