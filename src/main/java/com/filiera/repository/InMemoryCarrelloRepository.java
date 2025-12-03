package com.filiera.repository;

import com.filiera.model.payment.Carrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InMemoryCarrelloRepository extends JpaRepository<Carrello, UUID> {

    Optional<Carrello> findByBuyerId(UUID buyerId);

    @Query("SELECT c FROM Carrello c JOIN c.products p WHERE p.product.id = :productId")
    List<Carrello> findCarrelliContainingProduct(@Param("productId") UUID productId);


}
