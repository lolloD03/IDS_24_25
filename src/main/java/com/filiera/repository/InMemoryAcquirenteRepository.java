package com.filiera.repository;

import com.filiera.model.users.Acquirente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InMemoryAcquirenteRepository extends JpaRepository<Acquirente, UUID> {

}
