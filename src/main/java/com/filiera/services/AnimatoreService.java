package com.filiera.services;


import com.filiera.exception.EventNotFoundException;
import com.filiera.exception.ProductNotFoundException;
import com.filiera.exception.UserNotFoundException;
import com.filiera.model.dto.EventoRequestDTO;
import com.filiera.model.dto.EventoSimpleDTO;
import com.filiera.model.events.AnimatoreFiliera;
import com.filiera.model.events.Evento;
import com.filiera.model.events.Invito;
import com.filiera.model.events.StatoInvito;
import com.filiera.model.products.Prodotto;
import com.filiera.model.sellers.Venditore;
import com.filiera.model.users.Acquirente;
import com.filiera.model.users.User;
import com.filiera.repository.InMemoryEventRepository;
import com.filiera.repository.InMemoryInvitoRepository;
import com.filiera.repository.InMemoryUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnimatoreService implements EventQueryService {

    private final InMemoryEventRepository eventoRepo;
    private final InMemoryUserRepository userRepo;
    private final InMemoryInvitoRepository invitoRepo;

    public AnimatoreService(InMemoryEventRepository eventoRepo, InMemoryUserRepository userRepo, InMemoryInvitoRepository invitoRepo) {
        this.eventoRepo = eventoRepo;
        this.userRepo = userRepo;
        this.invitoRepo = invitoRepo;
    }

    public Evento createEvent(EventoRequestDTO request, UUID organizerId) {
        AnimatoreFiliera organizer = (AnimatoreFiliera) userRepo.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException("Animatore della Filiera non trovato."));

        Evento evento = Evento.builder()
                .name(request.getName())
                .description(request.getDescription())
                .eventDate(request.getEventDate())
                .location(request.getLocation())
                .organizer(organizer)
                .build();

        organizer.addEvent(evento);
        return eventoRepo.save(evento);
    }


    public Invito inviteSellerToEvent(UUID eventId, UUID userId) {
        Evento evento = eventoRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento non trovato con id: " + eventId));

        Venditore userToInvite = (Venditore) userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con id: " + userId));

        if (invitoRepo.existsByEventoAndUser(evento, userToInvite)) {
            throw new IllegalStateException("L'utente è già stato invitato a questo evento.");
        }

        Invito nuovoInvito = Invito.builder()
                .evento(evento)
                .user(userToInvite)
                .stato(StatoInvito.IN_ATTESA)
                .build();

        evento.addInvito(nuovoInvito);

        Invito invito = invitoRepo.save(nuovoInvito);

        evento.attach(userToInvite);
        evento.notifyObservers();

        return invito;
    }

    public boolean checkPublisher(UUID organizerId, UUID eventId) {
        Evento evento = eventoRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento non trovato con id: " + eventId));
        AnimatoreFiliera organizer = (AnimatoreFiliera) userRepo.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException("Animatore della Filiera non trovato."));

        return evento.getOrganizer().equals(organizer);
    }

    public void deleteEvent(UUID eventId) {
        Evento eventToDelete = eventoRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento non trovato con id: " + eventId));

        eventToDelete.getOrganizer().removeEvent(eventToDelete);
        eventoRepo.delete(eventToDelete);
    }

    @Override
    public List<EventoSimpleDTO> getEventsDTO() {
        List<Evento> eventi = eventoRepo.findAll();

        List<EventoSimpleDTO> eventiSimple = eventi.stream().map(evento -> new EventoSimpleDTO(evento.getId(),
                evento.getName(),
                evento.getLocation(),
                evento.getDescription(),
                evento.getEventDate())
        ).toList();


        return eventiSimple;
    }

    @Override
    public List<EventoSimpleDTO> getEventsRegisteredByUser(UUID buyerId) {
        Acquirente buyer = (Acquirente) userRepo.findById(buyerId)
                .orElseThrow(() -> new UserNotFoundException("Acquirente non trovato: " + buyerId));
        List<Evento> eventi = eventoRepo.findByRegisteredBuyersContains(buyer);


        return eventi.stream().map(evento ->
                new EventoSimpleDTO(evento.getId(),
                evento.getName(),
                evento.getLocation(),
                evento.getDescription(),
                evento.getEventDate()))
                .toList();
    }

    public Optional<Evento> getByIdEntity(UUID id) {
        return eventoRepo.findById(id);
    }

    public void deleteExpiredEvents(){
        LocalDate today = LocalDate.now();

        List<UUID> expiredEvents = eventoRepo.findByEventDateBefore(today)
                .stream()
                .map(Evento::getId)
                .toList();

        if (expiredEvents.isEmpty()) {
            return;
        }

        for (UUID id : expiredEvents) {
            executeDeletionLogic(getByIdEntity(id).orElseThrow(() -> new EventNotFoundException("Event with ID " + id + " doesn't exist.")));
        }

    }

    public void executeDeletionLogic(Evento event){

        event.getOrganizer().removeEvent(event);
        eventoRepo.deleteById(event.getId());

    }
}