package com.autotests.lichessbackend.controller;

import com.autotests.lichessbackend.dto.AddPlayerDto;
import com.autotests.lichessbackend.service.PlayerService;
import com.autotests.lichessbackend.service.StatsService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
        log.info("Opening players page");
        log.debug("Loading players with pageable: {}", pageable);

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
        log.info("Add player request from UI");
        log.debug("Player form username: {}", playerForm.getUsername());

        if (bindingResult.hasErrors()) {
            log.warn("Validation failed while adding player: {}", bindingResult.getAllErrors());

            model.addAttribute("players", playerService.getAllPlayers(pageable));
            model.addAttribute("playerForm", playerForm);

            return "players";
        }

        playerService.addPlayer(playerForm);
        log.info("Player added successfully: {}", playerForm.getUsername());

        return "redirect:/players-page";
    }

    @PostMapping("/players-page/{username}/sync")
    public String syncPlayer(@PathVariable String username) {
        log.info("Sync requested from UI for player: {}", username);

        playerService.syncPlayerGames(username);

        log.info("Sync finished from UI for player: {}", username);

        return "redirect:/players-page";
    }

    @GetMapping("/players-page/{username}/stats")
    public String playerStats(@PathVariable String username, Model model) {
        log.info("Opening stats page for player: {}", username);

        model.addAttribute("stats", statsService.getStats(username));

        return "player-stats";
    }
}