package com.filiera.repository;

import com.filiera.model.payment.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InMemoryOrdineRepository extends JpaRepository<Ordine, UUID> {
}
