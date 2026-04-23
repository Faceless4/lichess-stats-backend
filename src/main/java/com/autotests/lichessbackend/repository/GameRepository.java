package com.autotests.lichessbackend.repository;


import com.autotests.lichessbackend.entity.Game;
import com.autotests.lichessbackend.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {
    Optional<Game> findByLichessGameId(String lichessGameId);
    List<Game> findAllByPlayer(Player player);
    long countByPlayer(Player player);
}
