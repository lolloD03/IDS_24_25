package com.filiera.controller;
import com.filiera.model.dto.EventoSimpleDTO;
import com.filiera.model.dto.PacchettoResponseDTO;
import com.filiera.model.dto.ProductResponseDTO;
import com.filiera.model.dto.VenditoreIndirizzoDTO;
import com.filiera.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/public")
public class PublicController {

    private final ProdottoQueryService service;
    private final MapService mapService;
    private final PacchettoQueryService pacchettoService;
    private final EventQueryService eventQueryService;

    public PublicController(EventQueryService eventQueryService,ProdottoQueryService service, MapService mapService, PacchettoQueryService pacchettoService) {
        this.service = service;
        this.mapService = mapService;
        this.pacchettoService = pacchettoService;
        this.eventQueryService = eventQueryService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponseDTO>> getApprovedProducts() {
        List<ProductResponseDTO> products = service.getApprovedProducts();
        return ResponseEntity.ok(products);
    }
    @GetMapping("/bundles")
    public ResponseEntity<List<PacchettoResponseDTO>> getBundles() {
        List<PacchettoResponseDTO> bundles = pacchettoService.getAllPacchetti();
        return ResponseEntity.ok(bundles);
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

    @GetMapping("/eventi")
    public ResponseEntity<List<EventoSimpleDTO>> getAllEvents() {
        List<EventoSimpleDTO> eventi = eventQueryService.getEventsDTO();
        return ResponseEntity.ok(eventi);
    }

    @PostMapping("/{prodottoId}/condividi")
    public ResponseEntity<String> condividiProdotto(@PathVariable UUID prodottoId) {
        service.condividiProdotto(prodottoId);
        return ResponseEntity.ok("Prodotto condiviso sui social!");
    }


}