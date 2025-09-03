package com.filiera.model.products;

import com.filiera.model.sellers.Venditore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Pacchetto {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    private int availableQuantity;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Venditore seller;

    // Relazione molti a molti con i prodotti.
    // Viene creata una tabella di join per gestire il mapping.
    @ManyToMany
    @JoinTable(
            name = "pacchetto_prodotto",
            joinColumns = @JoinColumn(name = "pacchetto_id"),
            inverseJoinColumns = @JoinColumn(name = "prodotto_id")
    )
    @Builder.Default
    private Set<Prodotto> products = new HashSet<>();
}