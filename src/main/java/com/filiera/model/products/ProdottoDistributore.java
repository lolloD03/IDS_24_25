package com.filiera.model.products;

import java.util.ArrayList;
import java.util.List;

public class ProdottoDistributore {

    private List<Prodotto> prodotti;

    public ProdottoDistributore() {
        this.prodotti = new ArrayList<>();
    }

    public ProdottoDistributore(List<Prodotto> prodotti) {
        this.prodotti = prodotti;
    }

    public void addToProdotti(Prodotto prodotto) {
        prodotti.add(prodotto);
    }

    public void removeFromProdotti(Prodotto prodotto) {
        prodotti.remove(prodotto);
    }

    public List<Prodotto> getProdotti() {
        return prodotti;
    }

    public void setProdotti(List<Prodotto> prodotti) {
        this.prodotti = prodotti;
    }

}
