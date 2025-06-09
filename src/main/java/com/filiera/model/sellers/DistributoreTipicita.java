package com.filiera.model.sellers;

import com.filiera.model.products.Prodotto;
import com.filiera.model.users.RuoloUser;

import java.util.List;
import java.util.UUID;

public class DistributoreTipicita extends Venditore {

    private List<Prodotto> productsBundle;
    private List<List<Prodotto>> listOfBundles;

    public DistributoreTipicita() {
        super();
    }


    public DistributoreTipicita(String password, String email, String name, RuoloUser ruoloUser, int partitaIva) {
        super( password, email, name, ruoloUser , partitaIva);
    }

    public void addToBundle(Prodotto prodotto) {
        productsBundle.add(prodotto);
    }

    public void removeFromBundle(Prodotto prodotto) {productsBundle.remove(prodotto);}

    public void addToListOfBundles(List<Prodotto> bundleOfproducts) {listOfBundles.add(bundleOfproducts);}

    public void removeFromListOfBundles(List<Prodotto> bundleOfproducts) {listOfBundles.remove(bundleOfproducts);}

}

