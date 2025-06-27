package com.filiera.model.products;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("PRODOTTODISTRIBUTORE")
public class ProdottoDistributore extends Prodotto{

    @OneToMany
    private List<Prodotto> products;

    public ProdottoDistributore() {
        this.products = new ArrayList<>();
    }

    public ProdottoDistributore(List<Prodotto> products) {
        this.products = products;
    }

    public void addToProducts(Prodotto product) {
        products.add(product);
    }

    public void removeFromProducts(Prodotto product) {
        products.remove(product);
    }

    public List<Prodotto> getProducts() {
        return products;
    }

    public void setProducts(List<Prodotto> products) {
        this.products = products;
    }

}


