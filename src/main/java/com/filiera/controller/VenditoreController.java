package com.filiera.controller;

import com.filiera.model.dto.PacchettoRequestDTO;
import com.filiera.model.dto.PacchettoResponseDTO;
import com.filiera.model.dto.ProdottoRequestDTO;
import com.filiera.model.dto.ProductResponseDTO;
import com.filiera.model.products.Prodotto;
import com.filiera.model.sellers.Venditore;
import com.filiera.services.CurrentUserService;
import com.filiera.services.PacchettoService;
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
    private final PacchettoService pacchettoService;
    private final CurrentUserService currentUserService;

    @Autowired
    public VenditoreController(ProductService service,  PacchettoService pacchettoService, CurrentUserService currentUserService) {
        this.service = service;
        this.pacchettoService = pacchettoService;
        this.currentUserService = currentUserService;
    }


    @PostMapping("/create-bundle")
    public ResponseEntity<PacchettoResponseDTO> createPacchetto(@Valid @RequestBody PacchettoRequestDTO request) {
        // Ottieni l'ID del venditore autenticato dal contesto di sicurezza

        UUID sellerId = currentUserService.getCurrentUserId();
        // Chiama il service per la logica di business
        PacchettoResponseDTO createdPacchetto = pacchettoService.createPacchetto(request, sellerId);

        return new ResponseEntity<>(createdPacchetto, HttpStatus.CREATED);
    }

    @PostMapping("/create-product")
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestBody @Valid ProdottoRequestDTO productDTO) { // Simulazione header di autenticazione

        UUID sellerId = currentUserService.getCurrentUserId();

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


    /*
        @PostMapping("/create-product")
        public ResponseEntity<Prodotto> createProduct(
                @RequestParam @NotNull UUID venditor,
                @RequestParam @NotBlank String name,
                @RequestParam @NotBlank String descrizione,
                @RequestParam @DecimalMin(value = "0.01", message = "Price must be greater than 0") double price,
                @RequestParam @Min(value = 1, message = "Quantity must be greater than 0") int quantity,
                @RequestParam @NotBlank String certification) {

            Prodotto product = service.createProduct(venditor, name, descrizione, price, quantity, certification);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        }


     */
    @PutMapping("/update-product")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable UUID productId,
            @RequestBody @Valid ProdottoRequestDTO productDTO) { // Simulazione header di autenticazione


        UUID sellerId = currentUserService.getCurrentUserId();

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
        UUID sellerId = currentUserService.getCurrentUserId();
        service.deleteProduct(productId,sellerId);
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
