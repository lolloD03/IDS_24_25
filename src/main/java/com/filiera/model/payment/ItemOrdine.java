package com.filiera.model.payment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor // Necessario per JPA
public class ItemOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id; // ID univoco per questo specifico elemento dell'ordine

    // Dati "snapshot" del prodotto al momento dell'acquisto
    private UUID productId;     // L'ID del Prodotto originale, per riferimento
    private String productName;
    private double productPrice;
    private int quantity;

    @ManyToOne // Molti ItemOrdine possono appartenere a un solo Ordine
    @JoinColumn(name = "ordine_id", nullable = false) // Colonna FK che punta all'Ordine
    private Ordine order; // Riferimento all'Ordine a cui appartiene questo ItemOrdine

    // Costruttore per creare un ItemOrdine dallo snapshot del Carrello/Prodotto
    public ItemOrdine(UUID productId, String productName, double productPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public double getTotal() {
        return this.productPrice * this.quantity;
    }
}
