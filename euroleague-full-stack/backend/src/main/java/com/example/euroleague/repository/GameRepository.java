package com.example.euroleague.repository;

import com.example.euroleague.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g WHERE g.gameDate = :date")
    List<Game> findByDate(@Param("date") LocalDate date);
}
