package com.filiera.repository;

import com.filiera.model.events.Evento;
import com.filiera.model.events.Invito;
import com.filiera.model.payment.Ordine;
import com.filiera.model.sellers.Venditore;
import com.filiera.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InMemoryInvitoRepository extends JpaRepository<Invito, UUID> {
    // Ci servirà per controllare i duplicati in modo efficiente
    boolean existsByEventoAndUser(Evento evento, User user);

    // Ci servirà per trovare un invito specifico
    Optional<Invito> findByEventoAndUser(Evento evento, User user);
    List<Invito> findByUser(Venditore user);
}
