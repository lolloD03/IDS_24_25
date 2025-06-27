package com.filiera.services;

import com.filiera.model.payment.Carrello;
import com.filiera.model.products.Prodotto;

import java.util.List;

public class CarrelloServiceImpl {

    private final Carrello carrello;
    private final ProductService productService;

    public CarrelloServiceImpl(InMemoryProductRepository prodRepo, ProductService productService) {
        this.productService = productService;
        this.carrello = new Carrello();

    }

    public List<Prodotto> addProduct(Prodotto prod) {
        if(productService.getById(prod.getId()).isEmpty()) {
            throw new RuntimeException("Il prodotto con id " + prod.getId() + " non esiste.");
        }

        this.carrello.addProduct(prod);
        return carrello.getProducts();
    }

    public List<Prodotto> removeProduct(Prodotto prod) {
        if(productService.getById(prod.getId()).isEmpty()) {
            throw new RuntimeException("Il prodotto con id " + prod.getId() + " non esiste.");
        }
        carrello.removeProduct(prod);
        return carrello.getProducts();
    }

    public StringBuilder getInvoice(Carrello carrello) {


        double totalInvoice = carrello.getTotalPrice();

        StringBuilder sb = new StringBuilder();

        sb.append("Fattura:\n");
        for(Prodotto prod : carrello.getProducts()) {
            sb.append("Prodotto: ").append(prod.getName()).append("\n");
            sb.append("Prezzo: ").append(prod.getPrice()).append("\n");
            sb.append("Quantità: ").append(prod.getAvailableQuantity()).append("\n");
            sb.append("Prezzo totale: ").append(prod.getPrice() * prod.getAvailableQuantity()).append("\n");
        }
        return sb;
    }

    public void clearCarrello(){
        if (this.carrello.getProducts().isEmpty()) {
            throw new RuntimeException("Il carrello è già vuoto.");
        }

        this.carrello.clearCarrello();
    }

    public Carrello getCarrello(){
        return this.carrello;
    }

}
