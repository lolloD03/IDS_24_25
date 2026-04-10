package com.filiera.repository;

import com.filiera.model.products.Certificazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InMemoryCertificazioneRepository extends JpaRepository<Certificazione, UUID> {
    Optional<Certificazione> findByNome(String nome);
}

