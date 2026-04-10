package com.filiera.services;

import com.filiera.exception.*;
import com.filiera.model.administration.Curatore;
import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;
import com.filiera.model.users.User;
import com.filiera.repository.InMemoryProductRepository;
import com.filiera.repository.InMemoryUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class CuratoreServiceImpl {

    private final InMemoryProductRepository productRepository;
    private final InMemoryUserRepository userRepository;

    @Autowired
    public CuratoreServiceImpl(InMemoryProductRepository productRepository, InMemoryUserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    @Transactional(readOnly = true)
    public List<Prodotto> getPendingProducts() {
        log.debug("Retrieving all pending products");
        return productRepository.findByState(StatoProdotto.PENDING_APPROVAL);
    }


    public Prodotto approveProduct(UUID productId, UUID curatorId) {
        log.info("Approving product {} by curator {}", productId, curatorId);

        validateIds(productId, curatorId);

        Prodotto product = findProductById(productId);
        validateProductForApproval(product);

        Curatore curator = findCuratorById(curatorId);

        product.approveBy(curator);
        Prodotto approvedProduct = productRepository.save(product);

        log.info("Product {} approved successfully by curator {}", productId, curatorId);
        return approvedProduct;
    }


    public Prodotto rejectProduct(UUID productId, UUID curatorId) {
        log.info("Rejecting product {} by curator {}", productId, curatorId);

        validateIds(productId, curatorId);

        Prodotto product = findProductById(productId);
        validateProductForApproval(product);

        Curatore curator = findCuratorById(curatorId);

        product.rejectBy(curator);
        Prodotto rejectedProduct = productRepository.save(product);

        log.info("Product {} rejected successfully by curator {}", productId, curatorId);
        return rejectedProduct;
    }


    @Transactional(readOnly = true)
    public List<Prodotto> getProductsByState(StatoProdotto productState) {
        log.debug("Retrieving products with state: {}", productState);
        return productRepository.findByState(productState);
    }


    @Transactional(readOnly = true)
    public List<Prodotto> getApprovedProducts() {
        log.debug("Retrieving approved products");
        return productRepository.findByState(StatoProdotto.APPROVED);
    }


    @Transactional(readOnly = true)
    public List<Prodotto> getRejectedProducts() {
        log.debug("Retrieving rejected products");
        return productRepository.findByState(StatoProdotto.REJECTED);
    }

    private void validateIds(UUID productId, UUID curatorId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID can't be null");
        }
        if (curatorId == null) {
            throw new IllegalArgumentException("Curator ID can't be null");
        }
    }

    private Prodotto findProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
    }

    private void validateProductForApproval(Prodotto product) {
        if (product.getState() != StatoProdotto.PENDING_APPROVAL) {
            throw new ProductNotPendingException(
                    "Product with ID " + product.getId() + " is not pending approval. Actual state: " + product.getState()
            );
        }
    }

    private Curatore findCuratorById(UUID curatorId) {
        User user = userRepository.findById(curatorId)
                .orElseThrow(() -> new UserNotFoundException("Curator with ID " + curatorId + " doesn't exist."));

        if (!(user instanceof Curatore)) {
            throw new InvalidUserTypeException("User with ID " + curatorId + " is not a curator.");
        }

        return (Curatore) user;
    }
}