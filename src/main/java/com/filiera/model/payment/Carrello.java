package com.filiera.model.payment;

import com.filiera.model.products.Prodotto;
import com.filiera.model.users.Acquirente;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Carrello {

    private UUID id;

    private List<Prodotto> products;

    private Acquirente buyer;

    private double totalPrice;

    public Carrello() {
        this.id = UUID.randomUUID();
        this.products = new ArrayList<>();
        this.buyer = new Acquirente();
    }

    public Carrello(List<Prodotto> products , Acquirente buyer) {
        this.id = UUID.randomUUID();
        this.products = products;
        this.buyer = buyer;
    }

    public UUID getId() {
        return id;
    }

    public List<Prodotto> getProducts() {
        return products;
    }

    public void setProducts(List<Prodotto> products) {
        this.products = products;
    }

    public Acquirente getBuyer() {
        return buyer;
    }

    public void setBuyer(Acquirente buyer) {
        this.buyer = buyer;
    }

    public double getTotalPrice() {

        return products.stream()
                .mapToDouble(Prodotto::getPrice)
                .sum();

    }

    public Prodotto addProduct(Prodotto product) {
        if(product==null){throw new RuntimeException("Il prodotto è nullo");}
        products.add(product);
        return product;
    }

    public Prodotto removeProduct(Prodotto product) {
        if(!products.contains(product)){throw new RuntimeException("Il prodotto non è nel carrello");}
        products.remove(product);
        return product;
    }

    public void clearCarrello() {
        products.clear();
    }

}
