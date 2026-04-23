package com.autotests.lichessbackend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SyncResponseDto {
    private String username;
    private int importedGames;
    private int totalStoredGames;
}
