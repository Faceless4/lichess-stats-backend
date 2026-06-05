package com.autotests.lichessbackend.service;

import com.autotests.lichessbackend.dto.AddPlayerDto;
import com.autotests.lichessbackend.dto.LichessGameDto;
import com.autotests.lichessbackend.dto.PlayerResponseDto;
import com.autotests.lichessbackend.dto.SyncResponseDto;
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

import java.util.List;

@Slf4j
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final LichessClient lichessClient;

    public PlayerService(PlayerRepository playerRepository,
                         GameRepository gameRepository,
                         LichessClient lichessClient) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.lichessClient = lichessClient;
    }

    @Transactional
    public SyncResponseDto syncPlayerGames(String username) {
        log.info("Starting sync for player: {}", username);

        Player player = playerRepository.findByUsernameIgnoreCase(username)
                .orElseGet(() -> {
                    log.debug("Player not found. Creating new player: {}", username);

                    Player newPlayer = new Player();
                    newPlayer.setUsername(username);

                    return playerRepository.save(newPlayer);
                });

        List<LichessGameDto> fetchedGames = lichessClient.fetchGamesByUsername(username, 30);

        log.debug("Fetched {} games from Lichess for player: {}", fetchedGames.size(), username);

        int importedCount = 0;

        for (LichessGameDto dto : fetchedGames) {
            if (gameRepository.findByLichessGameId(dto.getGameId()).isPresent()) {
                log.trace("Game already exists, skipping: {}", dto.getGameId());
                continue;
            }

            Game game = new Game();
            game.setLichessGameId(dto.getGameId());
            game.setOpponentUsername(dto.getOpponentUsername());
            game.setResult(dto.getResult());
            game.setOpeningName(dto.getOpeningName());
            game.setSpeed(dto.getSpeed());
            game.setRated(dto.getRated());
            game.setPlayedAt(dto.getPlayedAt());
            game.setPlayer(player);

            gameRepository.save(game);
            importedCount++;

            log.trace("Imported game: {}", dto.getGameId());
        }

        long totalStoredGames = gameRepository.countByPlayer(player);

        log.info(
                "Sync completed for player: {}. Imported: {}, total stored: {}",
                username,
                importedCount,
                totalStoredGames
        );

        return new SyncResponseDto(player.getUsername(), importedCount, (int) totalStoredGames);
    }

    @Transactional(readOnly = true)
    public Player getPlayerEntity(String username) {
        log.debug("Searching player entity by username: {}", username);

        return playerRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("Player not found: " + username));
    }

    @Transactional(readOnly = true)
    public PlayerResponseDto getPlayer(String username) {
        log.debug("Getting player response by username: {}", username);

        Player player = getPlayerEntity(username);
        long storedGames = gameRepository.countByPlayer(player);

        return new PlayerResponseDto(player.getId(), player.getUsername(), (int) storedGames);
    }

    @Transactional(readOnly = true)
    public Page<Player> getAllPlayers(Pageable pageable) {
        log.debug("Getting all players with pageable: {}", pageable);
        return playerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Player getPlayerById(Long id) {
        log.debug("Getting player by id: {}", id);

        return playerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Player not found with id: " + id));
    }

    @Transactional
    public Player addPlayer(AddPlayerDto dto) {
        log.info("Adding player: {}", dto.getUsername());

        Player player = new Player();
        player.setUsername(dto.getUsername());

        Player savedPlayer = playerRepository.save(player);

        log.info("Player saved with id: {}", savedPlayer.getId());

        return savedPlayer;
    }
}