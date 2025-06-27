package com.filiera.repository;

import com.filiera.model.payment.Carrello;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface InMemoryCarrelloRepository extends JpaRepository<Carrello, UUID> {

    Optional<Carrello> findByBuyerId(UUID buyerId);

}
