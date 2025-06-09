package com.filiera.services;

import com.filiera.model.products.Prodotto;
import com.filiera.model.sellers.Venditore;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface ProductService {
    Prodotto createProduct(Venditore venditore, String name, String descrizione, double price, int quantity);
    List<Prodotto> listAll();
    Optional<Prodotto> getById(UUID id);
    Prodotto updateProduct(Prodotto updatedProduct);
    void deleteProduct(Prodotto deletedProduct);

    public List<Prodotto> getApprovedProducts();


}