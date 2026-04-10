package com.filiera.model.products;

import com.filiera.model.sellers.Venditore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.*;

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


    @ElementCollection
    @Builder.Default
    private List<ProductSnapshot> productSnapshots = new ArrayList<>();

    public void addProduct(Prodotto prodotto) {
        productSnapshots.add(new ProductSnapshot(prodotto));
    }

    @Embeddable
    @Getter
    @Setter
    public static class ProductSnapshot {
        private String name;
        private String description;
        private double price;

        public ProductSnapshot() {}

        public ProductSnapshot(Prodotto prodotto) {
            this.name = prodotto.getName();
            this.description = prodotto.getDescription();
            this.price = prodotto.getPrice();
        }
    }
}