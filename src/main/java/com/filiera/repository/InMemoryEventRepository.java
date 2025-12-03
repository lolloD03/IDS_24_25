package com.filiera.repository;

import com.filiera.model.events.Evento;
import com.filiera.model.products.Prodotto;
import com.filiera.model.users.Acquirente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface InMemoryEventRepository extends JpaRepository<Evento, UUID> {

    List<Evento> findByRegisteredBuyersContains(Acquirente acquirente);
    List<Evento> findByEventDateBefore(LocalDate now);

}
