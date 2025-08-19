package com.example.euroleague.controller;

import com.example.euroleague.model.Game;
import com.example.euroleague.repository.GameRepository;
import com.example.euroleague.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TeamRepository teamRepository;

    // Endpoint to create a new game
    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody Game game) {
        // Validate that both teams exist before creating the game
        Optional.ofNullable(game.getTeam1())
                .flatMap(team1 -> teamRepository.findById(team1.getId()))
                .orElseThrow(() -> new RuntimeException("Team 1 not found"));

        Optional.ofNullable(game.getTeam2())
                .flatMap(team2 -> teamRepository.findById(team2.getId()))
                .orElseThrow(() -> new RuntimeException("Team 2 not found"));

        Game savedGame = gameRepository.save(game);
        return ResponseEntity.status(201).body(savedGame);
    }

    // Endpoint to create multiple games (bulk insertion)
    @PostMapping("/bulk")
    public ResponseEntity<?> createMultipleGames(@RequestBody List<Game> games) {
        // Get all team IDs from the list of games
        List<Long> teamIds = games.stream()
                .flatMap(game -> List.of(game.getTeam1().getId(), game.getTeam2().getId()).stream())
                .distinct()
                .toList();

        // Validate that all teams exist in the database
        List<Long> missingTeamIds = teamRepository.findAllById(teamIds).stream()
                .map(team -> team.getId())
                .toList();

        if (teamIds.size() != missingTeamIds.size()) {
            return ResponseEntity.badRequest().body("One or more teams not found.");
        }

        List<Game> savedGames = gameRepository.saveAll(games);
        return ResponseEntity.status(201).body(savedGames);
    }

    // Endpoint to get all games
    @GetMapping
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    // Endpoint to get the results between two teams
    @GetMapping("/results")
    public List<Game> getResults(@RequestParam Long teamId1, @RequestParam Long teamId2) {
        return gameRepository.findAll().stream()
                .filter(game -> (game.getTeam1().getId().equals(teamId1) && game.getTeam2().getId().equals(teamId2)) ||
                        (game.getTeam1().getId().equals(teamId2) && game.getTeam2().getId().equals(teamId1)))
                .toList();
    }

    // Endpoint to get games by a specific date
    @GetMapping("/by-date")
    public List<Game> getGamesByDate(@RequestParam String date) {
        // Parse the date and return games that match the date
        LocalDate parsedDate = LocalDate.parse(date);
        return gameRepository.findByDate(parsedDate);  // Assuming a `findByDate` method in GameRepository
    }
}
