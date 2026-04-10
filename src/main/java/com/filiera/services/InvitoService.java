package com.filiera.services;

import com.filiera.adapter.InvitoMapper;
import com.filiera.exception.InviteNotFoundException;
import com.filiera.exception.UserNotFoundException;
import com.filiera.model.dto.InvitoResponseDTO;
import com.filiera.model.sellers.Venditore;
import com.filiera.repository.InMemoryInvitoRepository;
import com.filiera.repository.InMemoryUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.filiera.model.events.Evento;
import com.filiera.model.events.Invito;
import com.filiera.model.events.StatoInvito;

import java.util.List;
import java.util.UUID;
@Service
public class InvitoService {
    private InMemoryInvitoRepository invitoRepo;
    private InvitoMapper invitoMapper;
    InMemoryUserRepository userRepo;

    public InvitoService(InMemoryUserRepository userRepo,InMemoryInvitoRepository invitoRepository, InvitoMapper invitoMapper) {
        this.invitoRepo = invitoRepository;
        this.invitoMapper = invitoMapper;
        this.userRepo = userRepo;
    }



    @Transactional
    public void respondToInvitation(UUID invitoId, StatoInvito risposta, UUID userId) {
        Invito invito = invitoRepo.findById(invitoId)
                .orElseThrow(() -> new InviteNotFoundException("Invito non trovato"));

        if (!invito.getUser().getId().equals(userId)) {
            throw new SecurityException("Non puoi rispondere a un invito non tuo.");
        }

        if (risposta != StatoInvito.ACCETTATO && risposta != StatoInvito.RIFIUTATO) {
            throw new IllegalArgumentException("Risposta non valida.");
        }

        invito.setStato(risposta);
        invitoRepo.save(invito);

    }

    public List<InvitoResponseDTO> getMyInvitations(UUID userId) {
        Venditore venditore = (Venditore) userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("Venditore non trovato"));
        List<Invito> inviti = invitoRepo.findByUser(venditore);
        List<InvitoResponseDTO> invitiDTO = inviti.stream().map(invitoMapper::toInvitoResponseDTO).toList();

        return invitiDTO;
    }
}
