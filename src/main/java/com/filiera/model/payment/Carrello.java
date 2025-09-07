
package com.filiera.model.payment;

import com.filiera.exception.EmptyCartException;
import com.filiera.exception.ProductNotFoundException;
import com.filiera.model.products.Prodotto;
import com.filiera.model.users.Acquirente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Carrello {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarrello> products = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY) // Eager potrebbe caricare troppi dati inutilmente
    @JoinColumn(name = "buyer_id", referencedColumnName = "id") // Nome della colonna FK e della PK di Acquirente
    private Acquirente buyer;


    public Carrello(List<ItemCarrello> products , Acquirente buyer) {
        this.products = products;
        this.buyer = buyer;
    }


    public double getTotalPrice() {

        if(products.isEmpty()) {
            throw new EmptyCartException("Cart Is Empty");
        }

        return products.stream().mapToDouble(ItemCarrello::getTotal).sum();

    }

    public void addProduct(Prodotto product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product can't be null");
        }

        // Prova a trovare un ItemCarrello esistente per il prodotto
        products.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst() // Trova il primo (e unico) match
                .ifPresentOrElse( // Se è presente, aggiorna la quantità
                        item -> item.increaseQuantity(quantity),
                        // Altrimenti, crea un nuovo ItemCarrello e aggiungilo
                        () -> {
                            ItemCarrello newItem = new ItemCarrello(product, quantity);
                            newItem.setCart(this); // Collega il nuovo item a questo carrello
                            products.add(newItem);
                        }
                );
    }

    public void removeProduct(Prodotto product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product can't be null");
        }

        // Trova l'ItemCarrello da modificare/rimuovere
        products.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> {
                            item.decreaseQuantity(quantity);
                            if (item.getQuantity() <= 0) {
                                // Rimuovi l'elemento dalla lista se la quantità scende a zero o meno
                                products.remove(item);
                                // Nota: Se ItemCarrello ha orphanRemoval=true, questo lo eliminerà dal DB.
                            }
                        },
                        // Se il prodotto non è nel carrello, potresti lanciare un'eccezione
                        // o semplicemente non fare nulla (dipende dalla logica di business desiderata)
                        () -> {
                            throw new ProductNotFoundException("Product " + product.getName() + " is not in the cart");
                        }
                );
    }

    public void clearCarrello() {
        products.clear();
    }


}
