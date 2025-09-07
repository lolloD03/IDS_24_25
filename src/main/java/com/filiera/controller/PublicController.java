package com.filiera.controller;
import com.filiera.model.dto.ProductResponseDTO;
import com.filiera.model.dto.VenditoreIndirizzoDTO;
import com.filiera.services.MapService;
import com.filiera.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final ProductService service;
    private final MapService mapService;

    public PublicController(ProductService service, MapService mapService) {
        this.service = service;
        this.mapService = mapService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getApprovedProducts() {
        List<ProductResponseDTO> products = service.getApprovedProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/map")
    public ResponseEntity<List<VenditoreIndirizzoDTO>> getVendorsForMap() {
        List<VenditoreIndirizzoDTO> vendors = mapService.getVendorAddressesWithNames();
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/map/{vendorId}")
    public ResponseEntity<String> getVendorAddress(@PathVariable UUID vendorId) {
        String address = mapService.getVendorAddressById(vendorId);
        return ResponseEntity.ok(address);
    }

}