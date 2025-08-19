package com.example.euroleague.controller;

import com.example.euroleague.model.Player;
import com.example.euroleague.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    // Get all players
    @GetMapping
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    // Get a player by ID
    @GetMapping("/{id}")
    public Player getPlayer(@PathVariable Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    // Create a new player
    @PostMapping
    public Player createPlayer(@RequestBody Player player) {
        return playerRepository.save(player);
    }

    // Create multiple players (bulk creation)
    @PostMapping("/bulk")
    public ResponseEntity<List<Player>> createPlayers(@RequestBody List<Player> players) {
        List<Player> savedPlayers = playerRepository.saveAll(players); // Save all players at once
        return new ResponseEntity<>(savedPlayers, HttpStatus.CREATED); // Return the saved players with CREATED status
    }

    // Update an existing player
    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player player) {
        player.setId(id);
        return playerRepository.save(player);
    }

    // Delete a player by ID
    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerRepository.deleteById(id);
    }
}
