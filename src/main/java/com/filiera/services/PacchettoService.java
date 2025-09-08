package com.filiera.services;

import com.filiera.exception.InsufficientQuantityException;
import com.filiera.exception.ProductNotFoundException;
import com.filiera.exception.SellerNotFoundException;

import com.filiera.adapter.PacchettoMapper;
import com.filiera.model.dto.PacchettoRequestDTO;
import com.filiera.model.dto.PacchettoResponseDTO;
import com.filiera.model.products.Pacchetto;
import com.filiera.model.products.Prodotto;
import com.filiera.model.sellers.DistributoreTipicita;
import com.filiera.model.sellers.Venditore;
import com.filiera.repository.InMemoryPacchettoRepository;
import com.filiera.repository.InMemoryProductRepository;
import com.filiera.repository.InMemoryUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PacchettoService {

    private final InMemoryPacchettoRepository pacchettoRepo;
    private final InMemoryProductRepository prodottoRepo;
    private final InMemoryUserRepository venditoreRepo;
    private final PacchettoMapper pacchettoMapper;
    private final ProductServiceImpl productService;


    public PacchettoService(InMemoryPacchettoRepository pacchettoRepo, InMemoryProductRepository prodottoRepo, InMemoryUserRepository venditoreRepo, PacchettoMapper pacchettoMapper, ProductServiceImpl prodService) {
        this.pacchettoRepo = pacchettoRepo;
        this.prodottoRepo = prodottoRepo;
        this.venditoreRepo = venditoreRepo;
        this.pacchettoMapper = pacchettoMapper;
        this.productService = prodService;

    }

    @Transactional
    public PacchettoResponseDTO createPacchetto(PacchettoRequestDTO request, UUID sellerId) {
        // 1. Trova il venditore che sta creando il pacchetto (dal contesto di sicurezza)
        Venditore seller = (DistributoreTipicita) venditoreRepo.findById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException("Venditore non trovato con id: " + sellerId));

        // 2. Crea l'entità Pacchetto
        Pacchetto pacchetto = Pacchetto.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .seller(seller)
                .build();

        // 3. Trova i prodotti per ID e aggiungili al pacchetto
        request.getProductIds().forEach(productId -> {
            Prodotto prodotto = prodottoRepo.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Prodotto non trovato con id: " + productId));
            productService.checkProductState(productId);
            pacchetto.getProducts().add(prodotto); // OPZIONALE , CREARE UN METODO SU PACCHETTO INVECE CHE UTILIZZARE IL METODO DA QUA
        });

        // 4. Salva il pacchetto
        Pacchetto savedPacchetto = pacchettoRepo.save(pacchetto);

        // 5. Usa il mapper per convertire l'entità in DTO per la risposta
        return pacchettoMapper.toDTO(savedPacchetto);
    }


    public void decreaseQuantity(UUID pacchettoId, int quantityToDeduct) {
        Pacchetto pacchetto = pacchettoRepo.findById(pacchettoId)
                .orElseThrow(() -> new ProductNotFoundException("Pacchetto not found with id: " + pacchettoId));

        if (pacchetto.getAvailableQuantity() < quantityToDeduct) {
            throw new InsufficientQuantityException("Insufficient quantity for package: " + pacchetto.getName());
        }

        pacchetto.setAvailableQuantity(pacchetto.getAvailableQuantity() - quantityToDeduct);
        pacchettoRepo.save(pacchetto);
    }
}