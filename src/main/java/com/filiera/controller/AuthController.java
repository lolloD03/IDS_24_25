package com.filiera.controller;

import com.filiera.model.OsmMap.Indirizzo;
import com.filiera.model.administration.Curatore;
import com.filiera.model.dto.RegisterRequest;
import com.filiera.model.dto.RegisterRequestAcquirente;
import com.filiera.model.dto.RegisterRequestProduttore;
import com.filiera.model.sellers.Produttore;
import com.filiera.model.users.Acquirente;
import com.filiera.model.users.RuoloUser;
import com.filiera.model.users.User;
import com.filiera.repository.InMemoryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final InMemoryUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/registerCuratore")
    public ResponseEntity<?> registerCuratore(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email già registrata");
        }

        User user = Curatore.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 🔑 sempre codificata
                .role(RuoloUser.CURATORE)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok("Registrazione avvenuta con successo!");
    }

    @PostMapping("/registerAcquirente")
    public ResponseEntity<?> registerAcquirente(@RequestBody RegisterRequestAcquirente request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email già registrata");
        }

        Indirizzo indirizzo = new Indirizzo(request.getCittà(),request.getVia(), request.getNumeroCivico());

        User user = Acquirente.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 🔑 sempre codificata
                .indirizzo(indirizzo)
                .role(RuoloUser.ACQUIRENTE)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok("Registrazione avvenuta con successo!");
    }

    @PostMapping("/registerProduttore")
    public ResponseEntity<?> registerProduttore(@RequestBody RegisterRequestProduttore request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email già registrata");
        }

        Indirizzo indirizzo = new Indirizzo(request.getCittà(),request.getVia(), request.getNumeroCivico());

        User user = Produttore.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 🔑 sempre codificata
                .address(indirizzo)
                .partitaIva(request.getPartitaIva())
                .process(request.getProcesso())
                .role(RuoloUser.PRODUTTORE)

                .build();

        userRepository.save(user);

        return ResponseEntity.ok("Registrazione avvenuta con successo!");
    }

}