package com.filiera.controller;

import com.filiera.model.dto.CarrelloResponseDTO;
import com.filiera.model.dto.OrdineResponseDTO;
import com.filiera.model.dto.ProductResponseDTO;
import com.filiera.services.CarrelloServiceImpl;
import com.filiera.services.CurrentUserService;
import com.filiera.services.ProductService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/acquirente")
@Slf4j

public class AcquirenteController {

    private final CurrentUserService currentUserService;
    private final CarrelloServiceImpl service;
    private final ProductService productService;

    @Autowired
    public AcquirenteController(CarrelloServiceImpl service , CurrentUserService currentUserService, ProductService productService ) {this.service = service;
        this.currentUserService = currentUserService; this.productService = productService;}



    @PostMapping("/add")
    public ResponseEntity<CarrelloResponseDTO> addToCart(
            @RequestParam @NotNull(message = "Product Id cannot be null") UUID product,
            @RequestParam @Min( value = 1 , message = "Quantity must be at least 1") int quantity
    ) {
        UUID buyerId = currentUserService.getCurrentUserId();
        log.info("Adding product {} with quantity {} to cart for buyer {}", product, quantity, buyerId);

        CarrelloResponseDTO cart = service.addProduct(product, quantity , buyerId);

        log.info("Product successfully added to cart for buyer {}", buyerId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/remove")
    public ResponseEntity<CarrelloResponseDTO> removeFromCart(@RequestParam @NotNull(message = "Product ID cannot be null") UUID product,
                                                              @RequestParam @Min(value = 1, message = "Quantity must be at least 1") int quantity) {
        UUID buyerId = currentUserService.getCurrentUserId();


        log.info("Removing product {} with quantity {} from cart for buyer {}", product, quantity, buyerId);


        CarrelloResponseDTO cart = service.removeProduct(product, quantity , buyerId); // Il service restituisce già il DTO

        log.info("Product successfully removed from cart for buyer {}", buyerId);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> prodotti = productService.listAll();
        return ResponseEntity.ok(prodotti);
    }


    @PostMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        UUID buyerId = currentUserService.getCurrentUserId();
        log.info("Clearing cart for buyer {}", buyerId);

        service.clearCart(buyerId);

        log.info("Cart successfully cleared for buyer {}", buyerId);
        return ResponseEntity.noContent().build(); // 204 No Content è più appropriato
    }

    @GetMapping("/carrello")
    public ResponseEntity<CarrelloResponseDTO> getCart() {
        UUID buyerId = currentUserService.getCurrentUserId();
        log.debug("Retrieving cart for buyer {}", buyerId);

        CarrelloResponseDTO cartResponse = service.getCart(buyerId); // Il service restituisce già il DTO

        return ResponseEntity.ok(cartResponse);
    }


    @PostMapping("/buy")
    public ResponseEntity<OrdineResponseDTO> buyCart() {
        UUID buyerId = currentUserService.getCurrentUserId();
        log.debug("Buying cart for buyer {}", buyerId);

        OrdineResponseDTO order = service.buyCart(buyerId);
        return ResponseEntity.ok(order);

    }



}
