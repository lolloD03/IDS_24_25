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
@Getter // Genera i getter per i campi di Venditore
@Setter // Genera i setter per i campi di Venditore
@NoArgsConstructor // Genera il costruttore senza argomenti, necessario per JPA
@SuperBuilder // Permette di usare il pattern Builder e funziona con l'ereditarietà
@ToString(callSuper = true) // Genera un toString che include anche i campi della classe padre
public abstract class Venditore extends User { // Nota: Manteniamo abstract se ci sono sottoclassi concrete di Venditore

    @JsonManagedReference
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Prodotto> products = new ArrayList<>();

    @Embedded
    private Indirizzo address; // Assicurati di avere la classe Indirizzo con @Embeddable

    @Column(nullable = false, unique = true) // La partita IVA non può essere null e deve essere unica
    private String partitaIva;

    public void addProduct(Prodotto product) {
        this.products.add(product);
        product.setSeller(this); // Importante per la relazione bidirezionale @OneToMany
    }

    public void removeProduct(Prodotto product) {
        if (!this.products.contains(product)) {
            throw new ProductNotFoundException("Prodotto non presente nella lista.");
        }
        this.products.remove(product);
        product.setSeller(null); // Rimuove la relazione bidirezionale
    }
}