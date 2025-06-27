package com.filiera.repository;

import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InMemoryProductRepository extends JpaRepository<Prodotto, UUID> {

    List<Prodotto> findByState(StatoProdotto stato);


}
