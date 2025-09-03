package com.filiera.services;


import com.filiera.exception.EventNotFoundException;
import com.filiera.exception.UserNotFoundException;
import com.filiera.model.dto.EventoRequestDTO;
import com.filiera.model.events.AnimatoreFiliera;
import com.filiera.model.events.Evento;
import com.filiera.model.users.User;
import com.filiera.repository.InMemoryEventRepository;
import com.filiera.repository.InMemoryUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class AnimatoreService {

    private final InMemoryEventRepository eventoRepo;
    private final InMemoryUserRepository userRepo;

    public AnimatoreService(InMemoryEventRepository eventoRepo, InMemoryUserRepository userRepo) {
        this.eventoRepo = eventoRepo;
        this.userRepo = userRepo;
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

        Set<User> invitedUsers = new HashSet<>(userRepo.findAllById(request.getInvitedUserIds()));
        evento.setInvitedUsers(invitedUsers);
        // For each invited user, register them as an observer of this specific event
        invitedUsers.forEach(evento::attach);

        Evento savedEvento = eventoRepo.save(evento);

        // Notify all registered observers after the event is successfully saved
        savedEvento.notifyObservers();

        return savedEvento;
    }

    public void deleteEvent(UUID eventId) {
        // Find the event to ensure it exists before attempting to delete it
        Evento eventToDelete = eventoRepo.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento non trovato con id: " + eventId));

        // Delete the event
        eventoRepo.delete(eventToDelete);
    }
}