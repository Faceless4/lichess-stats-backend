package com.autotests.lichessbackend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LichessGameDto {
    private String gameId;
    private String opponentUsername;
    private String result;
    private String openingName;
    private String speed;
    private Boolean rated;
    private LocalDateTime playedAt;
}