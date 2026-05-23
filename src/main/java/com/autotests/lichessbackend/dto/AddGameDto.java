package com.autotests.lichessbackend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AddGameDto {
    private String lichessGameId;
    private String opponentUsername;
    private String result;
    private String openingName;
    private String speed;
    private Boolean rated;
    private LocalDateTime playedAt;
    private Long playerId;
}