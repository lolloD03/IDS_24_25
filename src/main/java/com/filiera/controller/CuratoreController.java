package com.filiera.controller;


import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;
import com.filiera.services.CuratoreServiceImpl;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/curatore")
@Validated
public class CuratoreController {

    private final CuratoreServiceImpl curatoreService;

    @Autowired
    public CuratoreController(CuratoreServiceImpl curatoreService) {
        this.curatoreService = curatoreService;
    }

    @GetMapping("/pending-products")
    public ResponseEntity<List<Prodotto>> getPendingProducts() {
        List<Prodotto> pendingProducts = curatoreService.getPendingProducts();
        return ResponseEntity.ok(pendingProducts);
    }

    @PutMapping("/approve-product")
    public ResponseEntity<Prodotto> approveProduct(
            @RequestParam @NotNull UUID productId,
            @RequestParam @NotNull UUID curatorId) {

        Prodotto approvedProduct = curatoreService.approveProduct(productId, curatorId);
        return ResponseEntity.ok(approvedProduct);
    }

    @PutMapping("/reject-product")
    public ResponseEntity<Prodotto> rejectProduct(
            @RequestParam @NotNull UUID productId,
            @RequestParam @NotNull UUID curatorId) {

        Prodotto rejectedProduct = curatoreService.rejectProduct(productId, curatorId);
        return ResponseEntity.ok(rejectedProduct);
    }

    @GetMapping("/products/by-state")
    public ResponseEntity<List<Prodotto>> getProductsByState(
            @RequestParam @NotNull StatoProdotto state) {

        List<Prodotto> products = curatoreService.getProductsByState(state);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/approved-products")
    public ResponseEntity<List<Prodotto>> getApprovedProducts() {
        List<Prodotto> approvedProducts = curatoreService.getApprovedProducts();
        return ResponseEntity.ok(approvedProducts);
    }

    @GetMapping("/rejected-products")
    public ResponseEntity<List<Prodotto>> getRejectedProducts() {
        List<Prodotto> rejectedProducts = curatoreService.getRejectedProducts();
        return ResponseEntity.ok(rejectedProducts);
    }
}