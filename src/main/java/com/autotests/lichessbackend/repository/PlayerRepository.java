package com.autotests.lichessbackend.repository;


import com.autotests.lichessbackend.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUsernameIgnoreCase(String username);
}