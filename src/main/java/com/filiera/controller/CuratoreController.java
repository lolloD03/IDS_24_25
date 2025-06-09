package com.filiera.controller;

import com.filiera.model.products.Prodotto;
import com.filiera.services.CuratoreServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CuratoreController {

    private final CuratoreServiceImpl curatoreService;

    public CuratoreController(CuratoreServiceImpl curatoreService) {
        this.curatoreService = curatoreService;
    }

    public List<Prodotto> getPendingProducts() {
        try {
            List<Prodotto> pendingProducts = curatoreService.getPendingProducts();

            if(pendingProducts == null||pendingProducts.isEmpty()) {
                System.out.println("No pending products found.");
                return Collections.emptyList();
            }
            return pendingProducts;

        } catch (Exception e) {
            System.out.println("Error retrieving pending products: " + e.getMessage());
            return null;

        }

    }

    public Prodotto approveProduct(Prodotto prodotto, UUID curatore) {
        try {
            return curatoreService.approveProduct(prodotto, curatore);
        } catch (Exception e) {
            System.out.println("Error approving product: " + e.getMessage());
            return null;
        }
    }

    public Prodotto rejectProduct(Prodotto prodotto, UUID curatore) {
        try {
            return curatoreService.rejectProduct(prodotto, curatore);
        } catch (Exception e) {
            System.out.println("Error rejecting product: " + e.getMessage());
            return null;
        }
    }

}
