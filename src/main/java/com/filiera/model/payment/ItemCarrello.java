package com.filiera.model.payment;

import com.filiera.model.products.Pacchetto;
import com.filiera.model.products.Prodotto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ItemCarrello {

    // Relazione con Prodotto. Sarà null se l'item è un Pacchetto.
    @ManyToOne
    @JoinColumn(name = "prodotto_id")
    private Prodotto product;

    // Relazione con Pacchetto. Sarà null se l'item è un Prodotto.
    @ManyToOne
    @JoinColumn(name = "pacchetto_id")
    private Pacchetto pacchetto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrello_id", nullable = false) // Assicura che l'ItemCarrello sia sempre legato a un Carrello
    private Carrello cart;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idItem;

    private int quantity;

    public ItemCarrello(Prodotto product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void increaseQuantity(int q) {
        this.quantity += q;
    }

    public void decreaseQuantity(int q) {
        this.quantity -= q;
    }

    public double getTotal() {
        return product.getPrice() * quantity;
    }

}