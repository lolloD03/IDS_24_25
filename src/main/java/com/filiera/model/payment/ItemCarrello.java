package com.filiera.model.payment;

import com.filiera.model.products.Pacchetto;
import com.filiera.model.products.Prodotto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ItemCarrello {

    @ManyToOne
    @JoinColumn(name = "prodotto_id")
    private Prodotto product;

    @ManyToOne
    @JoinColumn(name = "pacchetto_id")
    private Pacchetto pacchetto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrello_id", nullable = false)
    private Carrello cart;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idItem;

    private int quantity;

    public ItemCarrello(Prodotto product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ItemCarrello(Pacchetto pacchetto, int quantity) {
        this.pacchetto = pacchetto;
        this.quantity = quantity;
    }

    public void increaseQuantity(int q) {
        this.quantity += q;
    }

    public void decreaseQuantity(int q) {
        this.quantity -= q;
    }

    public double getTotal() {
        double totalPrice = 0;

        if (product != null) {
            totalPrice += product.getPrice() * quantity;
        } else if (pacchetto != null) {
            totalPrice += pacchetto.getPrice() * quantity;
        } else {
            throw new IllegalStateException("Né prodotto né pacchetto trovato.");
        }
return totalPrice;
    }

}