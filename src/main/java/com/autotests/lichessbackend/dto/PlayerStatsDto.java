package com.autotests.lichessbackend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class PlayerStatsDto {
    private String username;
    private long totalGames;
    private long wins;
    private long losses;
    private long draws;
    private Map<String, Long> mostPlayedOpenings;
}
