
package com.filiera.model.payment;

import com.filiera.exception.EmptyCartException;
import com.filiera.exception.ProductNotFoundException;
import com.filiera.model.products.Pacchetto;
import com.filiera.model.products.Prodotto;
import com.filiera.model.users.Acquirente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private Acquirente buyer;


    public Carrello(List<ItemCarrello> products , Acquirente buyer) {
        this.products = products;
        this.buyer = buyer;
    }


    public double getTotalPrice() {

        return products.stream().mapToDouble(ItemCarrello::getTotal).sum();

    }

    public void addProduct(Prodotto product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product can't be null");
        }

        products.stream()
                .filter(item -> item.getProduct() != null && item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.increaseQuantity(quantity),
                        () -> {
                            ItemCarrello newItem = new ItemCarrello(product, quantity);
                            newItem.setCart(this); // Collega il nuovo item a questo carrello
                            products.add(newItem);
                        }
                );
    }

    public void addBundle(Pacchetto product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product can't be null");
        }

        products.stream().filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.increaseQuantity(quantity),
                        ()->{
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

        Optional<ItemCarrello> itemOptional = products.stream()
                .filter(item -> item.getProduct() != null && item.getProduct().getId().equals(product.getId()))
                .findFirst();

        decreaseOrRemoveItem(itemOptional, quantity);
    }

    public void removeBundle(Pacchetto pacchetto, int quantity) {
        if (pacchetto == null) {
            throw new IllegalArgumentException("Bundle can't be null");
        }

        Optional<ItemCarrello> itemOptional = products.stream()
                .filter(item -> item.getPacchetto() != null && item.getPacchetto().getId().equals(pacchetto.getId()))
                .findFirst();

        decreaseOrRemoveItem(itemOptional, quantity);
    }

    private void decreaseOrRemoveItem(Optional<ItemCarrello> itemOptional, int quantity) {
        itemOptional.ifPresentOrElse(
                item -> {
                    item.decreaseQuantity(quantity);
                    if (item.getQuantity() <= 0) {
                        products.remove(item);
                    }
                },
                () -> {
                    throw new ProductNotFoundException("");
                }
        );
    }

    public void clearCarrello() {
        products.clear();
    }


}
