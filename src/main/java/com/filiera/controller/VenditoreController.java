package com.filiera.controller;

import com.filiera.model.dto.*;
import com.filiera.services.CurrentUserService;
import com.filiera.services.InvitoService;
import com.filiera.services.PacchettoService;
import com.filiera.services.ProductServiceImpl;
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

    private final ProductServiceImpl service;
    private final PacchettoService pacchettoService;
    private final CurrentUserService currentUserService;
    private final InvitoService invitoService;

    @Autowired
    public VenditoreController(InvitoService invitoService,ProductServiceImpl service,  PacchettoService pacchettoService, CurrentUserService currentUserService) {
        this.service = service;
        this.pacchettoService = pacchettoService;
        this.currentUserService = currentUserService;
        this.invitoService = invitoService;
    }


    @PostMapping("/create-bundle")
    public ResponseEntity<PacchettoResponseDTO> createPacchetto(@Valid @RequestBody PacchettoRequestDTO request) {
        UUID sellerId = currentUserService.getCurrentUserId();

        PacchettoResponseDTO createdPacchetto = pacchettoService.createPacchetto(request, sellerId);

        return new ResponseEntity<>(createdPacchetto, HttpStatus.CREATED);
    }

    @PostMapping("/create-product")
    public ResponseEntity<ProductResponseDTO> createProduct(
            @RequestBody @Valid ProdottoRequestDTO productDTO) {

        UUID sellerId = currentUserService.getCurrentUserId();

        ProductResponseDTO createdProduct = service.createProduct(productDTO, sellerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }


    @PutMapping("/update-product")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable UUID productId,
            @RequestBody @Valid ProdottoUpdateDTO productDTO) {

        UUID sellerId = currentUserService.getCurrentUserId();

        ProductResponseDTO product = service.updateProduct(productId, productDTO , sellerId);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/delete-product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        UUID sellerId = currentUserService.getCurrentUserId();
        service.deleteProduct(productId,sellerId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-bundle/{bundleId}")
    public ResponseEntity<Void> deleteBundle(@PathVariable UUID bundleId) {
        UUID sellerId = currentUserService.getCurrentUserId();
        pacchettoService.deletePacchetto(bundleId,sellerId);
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


    @PatchMapping("/evento/{invitoId}")
    public ResponseEntity<String> rispostaInvito(@PathVariable @NotNull UUID invitoId,
                                                 @Valid @RequestBody InvitoResponse responseDTO) {
        UUID sellerId = currentUserService.getCurrentUserId();

        invitoService.respondToInvitation(invitoId, responseDTO.getStatoInvito(), sellerId);

        return ResponseEntity.ok("Risposta all'invito registrata con successo.");
    }

    @GetMapping("/evento/visualizzaInviti")
    public ResponseEntity<List<InvitoResponseDTO>> visualizzaInviti() {
        UUID sellerId = currentUserService.getCurrentUserId();

        List<InvitoResponseDTO> inviti = invitoService.getMyInvitations(sellerId);
        return ResponseEntity.ok(inviti);

    }
}
