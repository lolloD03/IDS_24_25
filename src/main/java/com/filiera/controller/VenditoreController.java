package com.filiera.controller;

import com.filiera.model.products.Prodotto;
import com.filiera.model.sellers.Venditore;
import com.filiera.services.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class VenditoreController {
    private final ProductService service;

    public VenditoreController(ProductService service) { this.service = service; }


    public Prodotto createProduct(Venditore venditore, String name, String descrizione, double price, int quantity) {

        try {
            if (venditore == null || name == null || descrizione == null || price <= 0 || quantity <= 0) {
                throw new IllegalArgumentException("Invalid product details provided.");
            }

            return service.createProduct(venditore, name, descrizione, price, quantity);

        } catch (IllegalArgumentException e) {
            System.err.println("Error creating product: " + e.getMessage());
            return null;
        }

    }

    public Prodotto updateProduct(Prodotto updatedProduct) {

        try {
            return service.updateProduct(updatedProduct);
        } catch (Exception e) {
            System.err.println("Error updating product: " + e.getMessage());
            return null;
        }

    }

    public void deleteProduct(Prodotto toDeleteProduct) {

        try {
            service.deleteProduct(toDeleteProduct);
        } catch (IllegalArgumentException e) {
            System.err.println("Error updating product: " + e.getMessage());
        }
    }


    public List<Prodotto> getApprovedProducts() {
        return service.getApprovedProducts();
    }

    public Optional<Prodotto> getById(UUID id) { return service.getById(id); }

    public List<Prodotto> list() { return service.listAll(); }
}
