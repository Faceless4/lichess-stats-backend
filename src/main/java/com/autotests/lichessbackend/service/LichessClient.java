package com.autotests.lichessbackend.service;

import com.autotests.lichessbackend.dto.LichessGameDto;
import com.autotests.lichessbackend.exception.SyncException;
import com.autotests.lichessbackend.util.GameResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class LichessClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public LichessClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://lichess.org")
                .defaultHeader(HttpHeaders.ACCEPT, "application/x-ndjson")
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public List<LichessGameDto> fetchGamesByUsername(String username, int max) {
        try {
            String response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/games/user/{username}")
                            .queryParam("max", max)
                            .queryParam("opening", true)
                            .queryParam("moves", false)
                            .queryParam("clocks", false)
                            .queryParam("evals", false)
                            .queryParam("accuracy", false)
                            .build(username))
                    .retrieve()
                    .body(String.class);

            if (response == null || response.isBlank()) {
                return List.of();
            }

            List<LichessGameDto> games = new ArrayList<>();
            String[] lines = response.split("\\R");

            for (String line : lines) {
                if (line == null || line.isBlank()) {
                    continue;
                }

                JsonNode gameNode = objectMapper.readTree(line);
                games.add(mapGame(gameNode, username));
            }

            return games;
        } catch (Exception e) {
            throw new SyncException("Failed to fetch games from Lichess for user: " + username, e);
        }
    }

    private LichessGameDto mapGame(JsonNode gameNode, String username) {
        String gameId = gameNode.path("id").asText();

        JsonNode playersNode = gameNode.path("players");
        JsonNode whiteNode = playersNode.path("white").path("user");
        JsonNode blackNode = playersNode.path("black").path("user");

        String whiteName = whiteNode.path("name").asText("");
        String blackName = blackNode.path("name").asText("");

        boolean playerIsWhite = username.equalsIgnoreCase(whiteName);
        String opponentUsername = playerIsWhite ? blackName : whiteName;

        String winner = gameNode.path("winner").asText("");
        String result = resolveResult(playerIsWhite, winner);

        String openingName = gameNode.path("opening").path("name").asText("Unknown opening");
        String speed = gameNode.path("speed").asText("unknown");
        boolean rated = gameNode.path("rated").asBoolean(false);

        long createdAtMillis = gameNode.path("createdAt").asLong(0L);
        LocalDateTime playedAt = createdAtMillis == 0L
                ? null
                : LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAtMillis), ZoneId.systemDefault());

        return new LichessGameDto(
                gameId,
                opponentUsername == null || opponentUsername.isBlank() ? "Unknown" : opponentUsername,
                result,
                openingName,
                speed,
                rated,
                playedAt
        );
    }

    private String resolveResult(boolean playerIsWhite, String winner) {
        if (winner == null || winner.isBlank()) {
            return GameResult.DRAW;
        }

        if (playerIsWhite && "white".equalsIgnoreCase(winner)) {
            return GameResult.WIN;
        }

        if (!playerIsWhite && "black".equalsIgnoreCase(winner)) {
            return GameResult.WIN;
        }

        return GameResult.LOSS;
    }
}