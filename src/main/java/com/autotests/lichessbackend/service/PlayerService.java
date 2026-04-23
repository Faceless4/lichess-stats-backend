package com.autotests.lichessbackend.service;



import com.autotests.lichessbackend.dto.LichessGameDto;
import com.autotests.lichessbackend.dto.PlayerResponseDto;
import com.autotests.lichessbackend.dto.SyncResponseDto;
import com.autotests.lichessbackend.entity.Game;
import com.autotests.lichessbackend.entity.Player;
import com.autotests.lichessbackend.exception.NotFoundException;
import com.autotests.lichessbackend.repository.GameRepository;
import com.autotests.lichessbackend.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Player player = playerRepository.findByUsernameIgnoreCase(username)
                .orElseGet(() -> {
                    Player newPlayer = new Player();
                    newPlayer.setUsername(username);
                    return playerRepository.save(newPlayer);
                });

        List<LichessGameDto> fetchedGames = lichessClient.fetchGamesByUsername(username, 30);

        int importedCount = 0;

        for (LichessGameDto dto : fetchedGames) {
            if (gameRepository.findByLichessGameId(dto.getGameId()).isPresent()) {
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
        }

        long totalStoredGames = gameRepository.countByPlayer(player);

        return new SyncResponseDto(player.getUsername(), importedCount, (int) totalStoredGames);
    }

    @Transactional(readOnly = true)
    public Player getPlayerEntity(String username) {
        return playerRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException("Player not found: " + username));
    }

    @Transactional(readOnly = true)
    public PlayerResponseDto getPlayer(String username) {
        Player player = getPlayerEntity(username);
        long storedGames = gameRepository.countByPlayer(player);
        return new PlayerResponseDto(player.getId(), player.getUsername(), (int) storedGames);
    }
}
