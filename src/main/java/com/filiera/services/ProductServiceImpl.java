package com.filiera.services;

import com.filiera.exception.*;
import com.filiera.adapter.ProdottoMapper;
import com.filiera.model.dto.CertificazioneDTO;
import com.filiera.model.dto.ProdottoRequestDTO;
import com.filiera.model.dto.ProdottoUpdateDTO;
import com.filiera.model.dto.ProductResponseDTO;
import com.filiera.model.payment.Carrello;
import com.filiera.model.products.Certificazione;
import com.filiera.model.products.Prodotto;
import com.filiera.model.sellers.Venditore;
import com.filiera.repository.InMemoryCarrelloRepository;
import com.filiera.repository.InMemoryCertificazioneRepository;
import com.filiera.repository.InMemoryUserRepository;
import org.springframework.transaction.annotation.Transactional;
import com.filiera.model.products.StatoProdotto;
import com.filiera.repository.InMemoryProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class ProductServiceImpl implements ProdottoQueryService{

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final InMemoryCertificazioneRepository certificazioneRepository;
    private final InMemoryCarrelloRepository carrelloRepository;
    private final InMemoryProductRepository prodRepo;
    private final InMemoryUserRepository vendRepo;
    private final ProdottoMapper prodottoMapper;

    @Autowired
    public ProductServiceImpl(InMemoryCarrelloRepository carrelloRepository,InMemoryCertificazioneRepository certificazioneRepository,InMemoryProductRepository prodRepo, InMemoryUserRepository vendRepo,  ProdottoMapper prodottoMapper) {
        this.prodRepo = prodRepo;
        this.certificazioneRepository = certificazioneRepository;
        this.vendRepo = vendRepo;
        this.prodottoMapper = prodottoMapper;
        this.carrelloRepository = carrelloRepository;
    }




    private void applyCertificazioni(Prodotto prodotto, List<CertificazioneDTO> certificazioniDTOs) {

        Set<Certificazione> nuoveCertificazioni = new HashSet<>();

        for (CertificazioneDTO certDTO : certificazioniDTOs) {
            Certificazione cert = certificazioneRepository.findByNome(certDTO.getNome())
                    .orElseGet(() -> {
                        // Crea E SALVA la nuova certificazione se non esiste
                        Certificazione nuova = Certificazione.builder()
                                .nome(certDTO.getNome())
                                .build();
                        return certificazioneRepository.save(nuova);
                    });
            nuoveCertificazioni.add(cert);
        }


        prodotto.setCertificazioni(nuoveCertificazioni);
    }


    @Transactional(readOnly = true)
    public List<ProductResponseDTO> listAll() { // Changed return type
        logger.debug("Retrieving all products");
        return prodRepo.findAll().stream()
                .map(prodottoMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public Optional<ProductResponseDTO> getById(UUID id) {
        logger.debug("Retrieving product with id: {}", id);
        Prodotto prodotto = prodRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " doesn't exist."));
        return Optional.of(prodottoMapper.toDTO(prodotto));
    }


    public ProductResponseDTO createProduct(ProdottoRequestDTO productRequestDTO, UUID sellerId) {
        logger.info("Creating new product from request DTO for vendor: {}", sellerId);

        Venditore seller = (Venditore) vendRepo.findById(sellerId)
                .orElseThrow(() -> new UserNotFoundException("Seller not found with Id: " + sellerId));

        Prodotto product = prodottoMapper.toEntity(productRequestDTO, seller);

        if (productRequestDTO.getCertificazioni() != null) {
            applyCertificazioni(product, productRequestDTO.getCertificazioni());
        }

        Prodotto savedProduct = prodRepo.save(product);
        seller.addProduct(savedProduct);

        logger.info("Product created successfully with id: {}", savedProduct.getId());
        return prodottoMapper.toDTO(savedProduct);
    }


    public Optional<Prodotto> getByIdEntity(UUID id) {
        logger.debug("Retrieving product with id: {}", id);
        return prodRepo.findById(id);
    }


    public ProductResponseDTO updateProduct(UUID productId, ProdottoUpdateDTO productRequestDTO, UUID sellerId) {
        logger.info("Updating product with ID : {} for seller: {}", productId, sellerId);

        Prodotto existingProduct = prodRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        if (!existingProduct.getSeller().getId().equals(sellerId)) {
            logger.warn("Attempt not authorized updating product {} by seller {}. Product belongs to seller {}.",
                    productId, sellerId, existingProduct.getSeller().getId());
            throw new InvalidUserTypeException("Seller with ID " + sellerId +
                    " is not authorizet to update product with ID " + productId);
        }
        prodottoMapper.updateEntity(existingProduct, productRequestDTO);
        if (productRequestDTO.getCertificazioni() != null) {
            applyCertificazioni(existingProduct, productRequestDTO.getCertificazioni());
        }
        existingProduct.setState(StatoProdotto.PENDING_APPROVAL);
        prodRepo.save(existingProduct);
        logger.info("Prodotto aggiornato con successo con id: {}", existingProduct.getId());
        return prodottoMapper.toDTO(existingProduct);
    }


    public void deleteProduct(UUID productId, UUID sellerId) {


        logger.info("Deleting product with id: {}", productId);


        Prodotto product = prodRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        if (!product.getSeller().getId().equals(sellerId)) {
            logger.warn("Attempt not authorized updating product {} by seller {}. Product belongs to seller {}.",
                    productId, sellerId, product.getSeller().getId());
            throw new InvalidUserTypeException("Seller with ID " + sellerId +
                    " is not authorized to update product with ID " + productId);
        }

        executeDeletionLogic(product);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getApprovedProducts() { // Changed return type
        logger.debug("Retrieving approved products");
        return prodRepo.findByState(StatoProdotto.APPROVED).stream()
                .map(prodottoMapper::toDTO)
                .collect(Collectors.toList());
    }


    public void decreaseQuantity(UUID productId, int quantity) {
        logger.info("Reducing quantity for product: {} by {}", productId, quantity);

        if (quantity <= 0) {
            throw new InsufficientQuantityException("Quantity to decrease must be greater than zero.");
        }

        Prodotto product = prodRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " doesn't exist."));

        if (product.getAvailableQuantity() < quantity) {
            throw new InsufficientQuantityException("Unavailable quantity for product with ID " + productId +
                    ". Available: " + product.getAvailableQuantity() + ", Request: " + quantity);
        }

        int newQuantity = product.getAvailableQuantity() - quantity;
        product.setAvailableQuantity(newQuantity);

        if (newQuantity == 0) {
            product.setState(StatoProdotto.OUT_OF_STOCK);
            logger.info("Product {} is now out of stock", productId);
        }

        logger.info("Quantity reduced successfully for product: {}", productId);
    }

    @Transactional
    @Override
    public void condividiProdotto(UUID prodottoId) {
        Prodotto prodotto = prodRepo.findById(prodottoId)
                .orElseThrow(() -> new ProductNotFoundException("Prodotto non trovato"));

        prodotto.incrementaCondivisioni();
        prodRepo.save(prodotto);

        System.out.println("📢 Il prodotto " + prodotto.getName() +
                " è stato condiviso sui social! Totale condivisioni: " + prodotto.getNumeroCondivisioni());
    }

    @Transactional
    public void deleteExpiredProducts_Safe() {
        LocalDate today = LocalDate.now();

        List<UUID> expiredProductIds = prodRepo.findByExpirationDateBefore(today)
                .stream()
                .map(Prodotto::getId)
                .toList();

        if (expiredProductIds.isEmpty()) {
            return;
        }
        ;

        for (UUID id : expiredProductIds) {
            executeDeletionLogic(getByIdEntity(id).orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " doesn't exist.")));
        }
    }


    private void executeDeletionLogic(Prodotto product) {
        logger.info("Esecuzione logica di pulizia per prodotto: {}", product.getId());

        List<Carrello> carrelliAffetti = carrelloRepository.findCarrelliContainingProduct(product.getId());
        for (Carrello carrello : carrelliAffetti) {
            carrello.getProducts().removeIf(item -> product.getId().equals(
                    item.getProduct() != null ? item.getProduct().getId() : null));
            carrelloRepository.save(carrello);
        }

        product.getSeller().removeProduct(product);
        prodRepo.deleteById(product.getId());

        logger.info("Prodotto {} eliminato con successo.", product.getId());
    }


    public Prodotto checkProductState(UUID productId) {

        Prodotto product = prodRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + productId + " doesn't exist."));

        if(product.getState()==StatoProdotto.PENDING_APPROVAL)
            throw new ProductStateException("Can't buy a product with state 'pending_approval'");

        if(product.getState()==StatoProdotto.OUT_OF_STOCK)
            throw new ProductStateException("Can't buy a product with state 'out of stock'");

        if(product.getState()==StatoProdotto.REJECTED)
            throw new ProductStateException("Can't buy a product with state 'rejected'");

        return product;
    }

}