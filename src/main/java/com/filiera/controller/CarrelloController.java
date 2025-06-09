package com.filiera.controller;

import com.filiera.model.payment.Carrello;
import com.filiera.model.products.Prodotto;
import com.filiera.services.CarrelloServiceImpl;

public class CarrelloController {

    private final CarrelloServiceImpl service;

    public CarrelloController(CarrelloServiceImpl service) {
        this.service = service;
    }


    public Carrello addToCart(Prodotto prodotto) {
        try {
            if (prodotto == null) {
                throw new IllegalArgumentException("Product cannot be null");
            }
            service.addProduct(prodotto);
            return service.getCarrello();
        } catch (Exception e) {
            System.out.println("Error adding product to cart: " + e.getMessage());
            return null;
        }
    }

    public Carrello removeFromCart(Prodotto prodotto) {
        try {
            if (prodotto == null) {
                throw new IllegalArgumentException("Product cannot be null");
            }
            service.removeProduct(prodotto);
            return service.getCarrello();
        } catch (Exception e) {
            System.out.println("Error removing product from cart: " + e.getMessage());
            return null;
        }
    }

    public StringBuilder getInvoice(Carrello carrello){
        try {
            if (carrello == null) {
                throw new IllegalArgumentException("Cart cannot be null");
            }

            if (carrello.getProducts().isEmpty()) {
                throw new IllegalArgumentException("Cart is empty");
            }
            return service.getInvoice(carrello);
        } catch(Exception e) {
            System.out.println("Error getting invoice: " + e.getMessage());
        }

        return null;
    }

    public void clearCart() {
        try {
            service.clearCarrello();
        } catch (Exception e) {
            System.out.println("Error clearing cart: " + e.getMessage());
        }
    }

    public Carrello getCart() {
        return service.getCarrello();
    }

}