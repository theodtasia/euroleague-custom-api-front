package com.example.euroleague.controller;

import com.example.euroleague.model.Team;
import com.example.euroleague.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    // Get all teams
    @GetMapping
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    // Get a team by ID
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable Long id) {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            return ResponseEntity.ok(team.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new team
    @PostMapping
    public Team createTeam(@RequestBody Team team) {
        // You can add custom validation here
        return teamRepository.save(team);
    }

    // Update an existing team
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team team) {
        if (!teamRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        team.setId(id);
        Team updatedTeam = teamRepository.save(team);
        return ResponseEntity.ok(updatedTeam);
    }

    // Delete a team by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        if (!teamRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        teamRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Create multiple teams (Bulk insertion)
    @PostMapping("/bulk")
    public List<Team> createMultipleTeams(@RequestBody List<Team> teams) {
        return teamRepository.saveAll(teams);
    }
}
