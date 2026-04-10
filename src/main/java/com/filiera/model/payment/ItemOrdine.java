package com.filiera.model.payment;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ItemOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID productId;
    private String productName;
    private double productPrice;
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "ordine_id", nullable = false)
    private Ordine order;

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
