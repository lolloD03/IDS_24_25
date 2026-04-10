package com.filiera.services;

import com.filiera.exception.*;

import com.filiera.adapter.PacchettoMapper;
import com.filiera.model.dto.PacchettoRequestDTO;
import com.filiera.model.dto.PacchettoResponseDTO;
import com.filiera.model.payment.Carrello;
import com.filiera.model.payment.ItemCarrello;
import com.filiera.model.products.Pacchetto;
import com.filiera.model.products.Prodotto;
import com.filiera.model.sellers.DistributoreTipicita;
import com.filiera.model.sellers.Venditore;
import com.filiera.repository.InMemoryCarrelloRepository;
import com.filiera.repository.InMemoryPacchettoRepository;
import com.filiera.repository.InMemoryProductRepository;
import com.filiera.repository.InMemoryUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PacchettoService implements PacchettoQueryService{

    private final InMemoryCarrelloRepository carrelloRepository;
    private final InMemoryPacchettoRepository pacchettoRepo;
    private final InMemoryProductRepository prodottoRepo;
    private final InMemoryUserRepository venditoreRepo;
    private final PacchettoMapper pacchettoMapper;
    private final ProductServiceImpl productService;


    public PacchettoService(InMemoryCarrelloRepository carrelloRepository,InMemoryPacchettoRepository pacchettoRepo, InMemoryProductRepository prodottoRepo, InMemoryUserRepository venditoreRepo, PacchettoMapper pacchettoMapper, ProductServiceImpl prodService) {
        this.pacchettoRepo = pacchettoRepo;
        this.prodottoRepo = prodottoRepo;
        this.venditoreRepo = venditoreRepo;
        this.pacchettoMapper = pacchettoMapper;
        this.productService = prodService;
        this.carrelloRepository = carrelloRepository;

    }

    public Optional<Pacchetto> getPacchettoById(UUID id) {
        return pacchettoRepo.findById(id);
    }

    @Override
    public List<PacchettoResponseDTO> getAllPacchetti() {
        return pacchettoRepo.findAll().stream().map(pacchettoMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public PacchettoResponseDTO createPacchetto(PacchettoRequestDTO request, UUID sellerId) {
        DistributoreTipicita seller = (DistributoreTipicita) venditoreRepo.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Venditore non trovato con id: " + sellerId));

        Pacchetto pacchetto = Pacchetto.builder()
                .name(request.getName())
                .description(request.getDescription())
                .availableQuantity(request.getAvailableQuantity())
                .price(request.getPrice())
                .seller(seller)
                .build();

        request.getProductIds().forEach(productId -> {
            Prodotto prodotto = prodottoRepo.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Prodotto non trovato con id: " + productId));
            productService.checkProductState(productId);
            pacchetto.addProduct(prodotto); // OPZIONALE , CREARE UN METODO SU PACCHETTO INVECE CHE UTILIZZARE IL METODO DA QUA
        });

        Pacchetto savedPacchetto = pacchettoRepo.save(pacchetto);

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

    public void deletePacchetto(UUID pacchettoId, UUID sellerId) {

        DistributoreTipicita seller = (DistributoreTipicita) venditoreRepo.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Seller not found with Id: " + sellerId));

        Pacchetto pacchetto = pacchettoRepo.findById(pacchettoId)
                .orElseThrow(()->new ProductNotFoundException("Pacchetto not found with id: " + pacchettoId));

        if (!pacchetto.getSeller().getId().equals(sellerId)) {
            throw new InvalidUserTypeException("Seller with ID " + sellerId +
                    " is not authorized to update product with ID " + pacchettoId);
        }

        List<Carrello> carrelli = carrelloRepository.findAll();
        for (Carrello carrello : carrelli) {
            carrello.getProducts().removeIf(item -> pacchettoId.equals(
                    item.getPacchetto() != null ? item.getPacchetto().getId() : null));
        }
        seller.removePacchetto(pacchetto);
        pacchettoRepo.delete(pacchetto);
    }
}