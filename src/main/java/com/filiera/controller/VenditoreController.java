package com.filiera.controller;

import com.filiera.model.dto.ProdottoRequestDTO;
import com.filiera.model.dto.ProductResponseDTO;
import com.filiera.model.products.Prodotto;
import com.filiera.model.sellers.Venditore;
import com.filiera.services.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/venditore")
@Validated
public class VenditoreController {

    private final ProductService service;

    @Autowired
    public VenditoreController(ProductService service) {
        this.service = service;
    }

    @PostMapping("/create-product")
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestBody @Valid ProdottoRequestDTO productDTO,
            @RequestHeader("X-User-Id") UUID sellerId) { // Simulazione header di autenticazione

        ProductResponseDTO createdProduct = service.createProduct(productDTO, sellerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // Alternativa: endpoint temporaneo per testing
    @PostMapping("/create-product-test")
    public ResponseEntity<ProductResponseDTO> createProductForTesting(
            @RequestBody @Valid ProdottoRequestDTO productDTO,
            @RequestParam UUID sellerId) { // Parametro temporaneo per test

        ProductResponseDTO createdProduct = service.createProduct(productDTO, sellerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }


    @PutMapping("/update-product")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable UUID productId,
            @RequestBody @Valid ProdottoRequestDTO productDTO ,
            @RequestHeader("X-User-Id") UUID sellerId) { // Simulazione header di autenticazione


        ProductResponseDTO product = service.updateProduct(productId, productDTO , sellerId);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/update-product-testing")
    public ResponseEntity<ProductResponseDTO> updateProductForTesting(
            @PathVariable UUID productId,
            @RequestBody @Valid ProdottoRequestDTO productDTO ,
            @RequestParam UUID sellerId) { // Simulazione header di autenticazione



        ProductResponseDTO product = service.updateProduct(productId, productDTO , sellerId);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/delete-product")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        service.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/approved-products")
    public ResponseEntity<List<ProductResponseDTO>> getApprovedProducts() {
        List<ProductResponseDTO> products = service.getApprovedProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable @NotNull UUID id) { // Changed return type
        Optional<ProductResponseDTO> product = service.getById(id); // Service returns Optional<DTO>
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = service.listAll();
        return ResponseEntity.ok(products);
    }
}
