package com.filiera.repository;

import com.filiera.model.events.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InMemoryEventRepository extends JpaRepository<Evento, UUID> {
}
