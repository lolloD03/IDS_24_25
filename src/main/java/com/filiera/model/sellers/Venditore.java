package com.filiera.model.sellers;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.filiera.exception.ProductNotFoundException;
import com.filiera.model.OsmMap.Indirizzo;
import com.filiera.model.products.Prodotto;
import com.filiera.model.users.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public abstract class Venditore extends User {

    @JsonManagedReference
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Prodotto> products = new ArrayList<>();

    @Embedded
    private Indirizzo address;

    @Column(nullable = false, unique = true)
    private String partitaIva;

    public void addProduct(Prodotto product) {
        this.products.add(product);
        product.setSeller(this);
    }

    public void removeProduct(Prodotto product) {
        if (!this.products.contains(product)) {
            throw new ProductNotFoundException("Prodotto non presente nella lista.");
        }
        this.products.remove(product);
        product.setSeller(null);
    }
}