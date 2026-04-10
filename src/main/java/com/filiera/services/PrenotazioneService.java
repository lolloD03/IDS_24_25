package com.filiera.services;

import com.filiera.exception.EventNotFoundException;
import com.filiera.exception.UserNotFoundException;
import com.filiera.model.events.Evento;
import com.filiera.model.users.Acquirente;
import com.filiera.repository.InMemoryEventRepository;
import com.filiera.repository.InMemoryUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class PrenotazioneService {

    private final InMemoryEventRepository eventoRepo;
    private final InMemoryUserRepository userRepo;

    public PrenotazioneService(InMemoryEventRepository eventoRepo, InMemoryUserRepository userRepo) {
        this.eventoRepo = eventoRepo;
        this.userRepo = userRepo;
    }

    public void prenotaEvento(UUID eventId, UUID acquirenteId) {
        Evento evento = eventoRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento non trovato con id: " + eventId));

        Acquirente acquirente = (Acquirente) userRepo.findById(acquirenteId)
                .orElseThrow(() -> new UserNotFoundException("Acquirente non trovato con id: " + acquirenteId));

        evento.registerBuyer(acquirente);
        eventoRepo.save(evento);
    }

    public void rimuoviPrenotazioneEvento(UUID eventId, UUID acquirenteId) {
        Evento evento = eventoRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento non trovato con id: " + eventId));

        Acquirente acquirente = (Acquirente) userRepo.findById(acquirenteId)
                .orElseThrow(() -> new UserNotFoundException("Acquirente non trovato con id: " + acquirenteId));

        evento.removeBuyer(acquirente);
        eventoRepo.save(evento);
    }






}
