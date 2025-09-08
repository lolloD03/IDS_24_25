package com.filiera.services;

import com.filiera.exception.*;
import com.filiera.adapter.ProdottoMapper;
import com.filiera.model.dto.ProdottoRequestDTO;
import com.filiera.model.dto.ProductResponseDTO;
import com.filiera.model.products.Prodotto;
import com.filiera.model.sellers.Venditore;
import com.filiera.repository.InMemoryUserRepository;
import org.springframework.transaction.annotation.Transactional;
import com.filiera.model.products.StatoProdotto;
import com.filiera.repository.InMemoryProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final InMemoryProductRepository prodRepo;
    private final InMemoryUserRepository vendRepo;
    private final ProdottoMapper prodottoMapper;

    @Autowired
    public ProductServiceImpl(InMemoryProductRepository prodRepo, InMemoryUserRepository vendRepo,  ProdottoMapper prodottoMapper) {
        this.prodRepo = prodRepo;
        this.vendRepo = vendRepo;
        this.prodottoMapper = prodottoMapper;
    }





    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> listAll() { // Changed return type
        logger.debug("Retrieving all products");
        return prodRepo.findAll().stream()
                .map(prodottoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductResponseDTO> getById(UUID id) {
        logger.debug("Retrieving product with id: {}", id);
        Prodotto prodotto = prodRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " doesn't exist."));
        return Optional.of(prodottoMapper.toDTO(prodotto));
    }

    @Override
    public ProductResponseDTO createProduct(ProdottoRequestDTO productRequestDTO, UUID sellerId) {
        logger.info("Creating new product from request DTO for vendor: {}", sellerId);

        // Find seller
        Venditore seller = (Venditore) vendRepo.findById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException("Seller not found with Id: " + sellerId));

        // Create and save product
        Prodotto product = prodottoMapper.toEntity(productRequestDTO, seller);

        Prodotto savedProduct = prodRepo.save(product);
        seller.addProduct(savedProduct);

        logger.info("Product created successfully with id: {}", savedProduct.getId());
        return prodottoMapper.toDTO(savedProduct);
    }


    public Optional<Prodotto> getByIdEntity(UUID id) {
        logger.debug("Retrieving product with id: {}", id);
        // findById già restituisce Optional<Prodotto>, quindi non c'è bisogno di .or() per loggare Optional.empty()
        return prodRepo.findById(id);
    }

    @Override
    public ProductResponseDTO updateProduct(UUID productId, ProdottoRequestDTO productRequestDTO, UUID sellerId) {
        logger.info("Updating product with ID : {} for seller: {}", productId, sellerId);

        // 2. Trova il prodotto esistente
        Prodotto existingProduct = prodRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        // 3. Verifica la proprietà del venditore
        // È fondamentale assicurarsi che solo il venditore proprietario possa modificare il prodotto.
        if (!existingProduct.getSeller().getId().equals(sellerId)) {
            logger.warn("Attempt not authorized updating product {} by seller {}. Product belongs to seller {}.",
                    productId, sellerId, existingProduct.getSeller().getId());
            throw new InvalidUserTypeException("Seller with ID " + sellerId +
                    " is not authorizet to update product with ID " + productId);
        }

        // 4. Aggiorna i campi del prodotto
        existingProduct.setName(productRequestDTO.getName());
        existingProduct.setDescription(productRequestDTO.getDescription());
        existingProduct.setPrice(productRequestDTO.getPrice());
        existingProduct.setAvailableQuantity(productRequestDTO.getQuantity());
        existingProduct.setCertification(productRequestDTO.getCertification());
        existingProduct.setExpirationDate(productRequestDTO.getExpirationDate());
        existingProduct.setState(StatoProdotto.PENDING_APPROVAL); // Imposta lo stato a "In attesa di approvazione"
        // 5. Salva il prodotto aggiornato
        Prodotto updatedProduct = prodRepo.save(existingProduct);
        logger.info("Prodotto aggiornato con successo con id: {}", updatedProduct.getId());
        return prodottoMapper.toDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(UUID productId, UUID sellerId) {


        logger.info("Deleting product with id: {}", productId);

        // Check if product exists
        if (!prodRepo.existsById(productId)) {
            throw new ProductNotFoundException("Product with ID  " + productId + " doesn't exist.");
        }

        Venditore seller = (Venditore) vendRepo.findById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException("Seller not found with Id: " + sellerId));


        Prodotto product = prodRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        seller.removeProduct(product);
        prodRepo.deleteById(productId);
        logger.info("Product deleted successfully with id: {}", productId);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getApprovedProducts() { // Changed return type
        logger.debug("Retrieving approved products");
        return prodRepo.findByState(StatoProdotto.APPROVED).stream()
                .map(prodottoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
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

        prodRepo.save(product);
        logger.info("Quantity reduced successfully for product: {}", productId);
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