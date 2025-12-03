package com.filiera.controller;

import com.filiera.model.dto.RegisterRequestUser;
import com.filiera.model.dto.RegisterRequestVenditore;
import com.filiera.model.users.User;
import com.filiera.repository.InMemoryUserRepository;
import com.filiera.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final InMemoryUserRepository userRepository;
    private final AuthService authService;


    @PostMapping("/registerUser")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestUser request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email già registrata");
        }

        authService.createUserSimple(request);

        return ResponseEntity.ok("Registrazione " + request.getRuoloUser().name() + " avvenuta con successo!");
    }


    @PostMapping("/registerSeller")
    public ResponseEntity<String> registerSeller(@RequestBody RegisterRequestVenditore request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email già registrata");
        }

        authService.createSeller(request);

        return ResponseEntity.ok("Registrazione " + request.getRuoloUser().name() + " avvenuta con successo!");
    }




}