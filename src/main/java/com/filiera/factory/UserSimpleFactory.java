package com.filiera.factory;

import com.filiera.model.administration.Curatore;
import com.filiera.model.dto.RegisterRequestUser;
import com.filiera.model.events.AnimatoreFiliera;
import com.filiera.model.users.Acquirente;
import com.filiera.model.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

public class UserSimpleFactory implements UserFactory{


    @Override
    public User createUser(RegisterRequestUser request , PasswordEncoder passwordEncoder) {

        return switch (request.getRuoloUser()) {
            case CURATORE -> Curatore.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRuoloUser())
                    .approved(false)
                    .build();

            case ACQUIRENTE -> Acquirente.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRuoloUser())
                    .approved(true)
                    .build();

            case ANIMATORE -> AnimatoreFiliera.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRuoloUser())
                    .approved(false)
                    .build();

            default -> throw new IllegalArgumentException("Ruolo non valido per la registrazione semplice: " + request.getRuoloUser());
        };
    }

}
