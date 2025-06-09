package com.filiera.model.sellers;

import com.filiera.model.users.RuoloUser;
import com.filiera.model.users.User;
import com.filiera.model.products.Prodotto;

import java.util.ArrayList;
import java.util.List;

public abstract class Venditore extends User {

    private List<Prodotto> prodotti;
    private int partitaIva;

    public Venditore() {
        super();
        prodotti = new ArrayList<>();
    }

    public Venditore(String password, String email, String name, RuoloUser ruolo, int partitaIva) {
        super( password, email, name , ruolo);
        this.partitaIva = partitaIva;
        prodotti = new ArrayList<>();
    }

    public void setProdotti(List<Prodotto> prodotti) {this.prodotti = prodotti;}
    public List<Prodotto> getProdotti() {return prodotti;}
    public int getPartitaIva() {return partitaIva;}
    public void setPartitaIva(int nuovaPartitaIva) {partitaIva = nuovaPartitaIva;}
    protected void prod(Prodotto prodotto){prodotti.add(prodotto);}
    protected void removeProductFromInventory(Prodotto prodotto) {prodotti.remove(prodotto);}

}
