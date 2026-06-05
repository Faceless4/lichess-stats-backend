package com.autotests.lichessbackend.service;

import com.autotests.lichessbackend.dto.AddGameDto;
import com.autotests.lichessbackend.entity.Game;
import com.autotests.lichessbackend.entity.Player;
import com.autotests.lichessbackend.exception.NotFoundException;
import com.autotests.lichessbackend.repository.GameRepository;
import com.autotests.lichessbackend.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public GameService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public Page<Game> getAllGames(Pageable pageable) {
        log.debug("Getting all games with pageable: {}", pageable);
        return gameRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Game getGameById(Long id) {
        log.debug("Getting game by id: {}", id);

        return gameRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Game not found with id: " + id));
    }

    @Transactional
    public Game addGame(AddGameDto dto) {
        log.info("Adding game with lichessGameId: {}", dto.getLichessGameId());

        Player player = playerRepository.findById(dto.getPlayerId())
                .orElseThrow(() -> new NotFoundException("Player not found with id: " + dto.getPlayerId()));

        Game game = new Game();
        game.setLichessGameId(dto.getLichessGameId());
        game.setOpponentUsername(dto.getOpponentUsername());
        game.setResult(dto.getResult());
        game.setOpeningName(dto.getOpeningName());
        game.setSpeed(dto.getSpeed());
        game.setRated(dto.getRated());
        game.setPlayedAt(dto.getPlayedAt());
        game.setPlayer(player);

        Game savedGame = gameRepository.save(game);

        log.info("Game saved with id: {}", savedGame.getId());

        return savedGame;
    }
}