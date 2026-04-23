package com.autotests.lichessbackend.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerResponseDto {
    private Long id;
    private String username;
    private int storedGames;
}
