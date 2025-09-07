package com.filiera.repository;

import com.filiera.model.products.Pacchetto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InMemoryPacchettoRepository extends JpaRepository<Pacchetto, UUID> { }