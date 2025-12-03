package com.filiera.controller;

import com.filiera.model.dto.ProductResponseDTO;
import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;
import com.filiera.services.CuratoreServiceImpl;
import com.filiera.services.CurrentUserService;
import com.filiera.services.ProductServiceImpl;
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
    private final CurrentUserService currentUserService;
    private final ProductServiceImpl productService;

    @Autowired
    public CuratoreController(CuratoreServiceImpl curatoreService,  CurrentUserService currentUserService,ProductServiceImpl productService) {
        this.curatoreService = curatoreService;
        this.currentUserService = currentUserService;
        this.productService =  productService;
    }
    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> prodotti = productService.listAll();
        return ResponseEntity.ok(prodotti);
    }

    @PutMapping("/approve-product")
    public ResponseEntity<Prodotto> approveProduct(
            @RequestParam @NotNull UUID productId) {

        UUID curatorId = currentUserService.getCurrentUserId();

        Prodotto approvedProduct = curatoreService.approveProduct(productId, curatorId);
        return ResponseEntity.ok(approvedProduct);
    }

    @PutMapping("/reject-product")
    public ResponseEntity<Prodotto> rejectProduct(
            @RequestParam @NotNull UUID productId
    ) {
        UUID curatorId = currentUserService.getCurrentUserId();
        Prodotto rejectedProduct = curatoreService.rejectProduct(productId, curatorId);
        return ResponseEntity.ok(rejectedProduct);
    }

    @GetMapping("/products/by-state")
    public ResponseEntity<List<Prodotto>> getProductsByState(
            @RequestParam @NotNull StatoProdotto state) {

        List<Prodotto> products = curatoreService.getProductsByState(state);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/pending-approval")
    public ResponseEntity<List<Prodotto>> getProductsPendingApproval(
            @RequestParam @NotNull StatoProdotto state) {

        List<Prodotto> products = curatoreService.getProductsByState(StatoProdotto.PENDING_APPROVAL);
        return ResponseEntity.ok(products);
    }

}