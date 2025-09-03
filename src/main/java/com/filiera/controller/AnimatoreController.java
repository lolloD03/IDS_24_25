package com.filiera.controller;


import com.filiera.model.dto.EventoRequestDTO;

import com.filiera.model.events.Evento;
import com.filiera.model.users.User;
import com.filiera.services.AnimatoreService;
import com.filiera.services.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/animatore/eventi")
public class AnimatoreController {

    private final AnimatoreService animatoreService;
    private final CurrentUserService currentUserService;

    public AnimatoreController(AnimatoreService animatoreService, CurrentUserService currentUserService) {
        this.animatoreService = animatoreService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/create-event")
    public ResponseEntity<Evento> createEvent(@Valid @RequestBody EventoRequestDTO request) {
        UUID organizerId = currentUserService.getCurrentUserId();

        Evento newEvento = animatoreService.createEvent(request, organizerId);
        return new ResponseEntity<>(newEvento, HttpStatus.CREATED);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID eventId) {
        animatoreService.deleteEvent(eventId);
        // Return a 204 No Content status on successful deletion
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}