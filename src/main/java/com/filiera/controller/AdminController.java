package com.filiera.controller;

import com.filiera.exception.UserNotFoundException;
import com.filiera.model.users.User;
import com.filiera.repository.InMemoryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final InMemoryUserRepository userRepository;

    @PostMapping("/approve/{userId}")
    public ResponseEntity<String> approveUser(@PathVariable UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con id: " + userId));

        user.setApproved(true);
        userRepository.save(user);

        return ResponseEntity.ok("Utente approvato con successo!");
    }

    @GetMapping("/listaDaApprovare")
    public ResponseEntity<List<User>> getNotApprovedUser(){
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users.stream().filter(user -> !user.isApproved()).toList());
    }

}
