package com.filiera.services;

import com.filiera.model.administration.Curatore;
import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;

import java.util.List;
import java.util.UUID;

public class CuratoreServiceImpl {

    private final InMemoryProductRepository productRepository;
    private final InMemoryUserRepository userRepository;

    public CuratoreServiceImpl(InMemoryProductRepository productRepository, InMemoryUserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<Prodotto> getPendingProducts() {
        List<Prodotto> pendingProducts = productRepository.findByState(StatoProdotto.IN_ATTESA_DI_APPROVAZIONE);
        return pendingProducts;
    }

    public Prodotto approveProduct(Prodotto prodotto, UUID curatoreId) {

        // Check if the product is in the pending state
        if (prodotto.getState() != StatoProdotto.IN_ATTESA_DI_APPROVAZIONE) {
            throw new IllegalArgumentException("Il prodotto non è in attesa di approvazione.");
        }
        // Check if the curator exists
        if (userRepository.findById(curatoreId).isEmpty()) {
            throw new IllegalArgumentException("Il curatore con ID " + curatoreId + " non esiste.");
        }

        Curatore curatore  = (Curatore) userRepository.findById(curatoreId).get();

        // Approve the product by the curator
        prodotto.approveBy(curatore);

        // Save the updated product
        return productRepository.save(prodotto);

    }


    public Prodotto rejectProduct(Prodotto prodotto, UUID curatore) {
        // Check if the product is in the pending state
        if (prodotto.getState() != StatoProdotto.IN_ATTESA_DI_APPROVAZIONE) {
            throw new IllegalArgumentException("Il prodotto non è in attesa di approvazione.");
        }

        // Check if the curator exists
        if (userRepository.findById(curatore).isEmpty()) {
            throw new IllegalArgumentException("Il curatore con ID " + curatore + " non esiste.");
        }

        Curatore curatoreObj = (Curatore) userRepository.findById(curatore).get();

        // Reject the product by the curator
        prodotto.rejectBy(curatoreObj);

        // Save the updated product
        return productRepository.save(prodotto);
    }

}
