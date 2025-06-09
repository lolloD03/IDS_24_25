package ControllerTest;

import com.filiera.controller.VenditoreController;
import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;
import com.filiera.model.sellers.Produttore;
import com.filiera.model.sellers.Venditore;
import com.filiera.repository.InMemoryProductRepository;
import com.filiera.repository.InMemoryUserRepository;
import com.filiera.services.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VenditoreControllerTest {

    private VenditoreController controller;
    private InMemoryProductRepository productRepo;
    private InMemoryUserRepository userRepo;
    private Venditore venditore;

    @BeforeEach
    void setUp() {
        productRepo = new InMemoryProductRepository();
        userRepo = new InMemoryUserRepository();
        ProductServiceImpl productService = new ProductServiceImpl(productRepo, userRepo);
        controller = new VenditoreController(productService);

        venditore = new Produttore();
        venditore.setId(UUID.randomUUID());
        userRepo.save(venditore);
    }

    @Test
    void createProduct_shouldReturnCreatedProduct() {
        Prodotto prodotto = controller.createProduct(venditore, "Prodotto Test", "Descrizione", 10.0, 5);
        assertNotNull(prodotto);
        assertEquals("Prodotto Test", prodotto.getName());
    }

    @Test
    void createProduct_shouldReturnNullIfInvalidInput() {
        Prodotto prodotto = controller.createProduct(null, null, null, -1, -5);
        assertNull(prodotto);
    }

    @Test
    void updateProduct_shouldUpdateAndReturnProduct() {
        Prodotto prodotto = controller.createProduct(venditore, "Prod", "desc", 10.0, 3);
        prodotto.setName("Modificato");
        Prodotto updated = controller.updateProduct(prodotto);

        assertNotNull(updated);
        assertEquals("Modificato", updated.getName());
    }

    @Test
    void deleteProduct_shouldRemoveProduct() {
        Prodotto prodotto = controller.createProduct(venditore, "Prod", "desc", 10.0, 3);
        controller.deleteProduct(prodotto);

        Optional<Prodotto> retrieved = controller.getById(prodotto.getId());
        assertTrue(retrieved.isEmpty());
    }

    @Test
    void getApprovedProducts_shouldReturnOnlyApproved() {
        Prodotto p1 = controller.createProduct(venditore, "Prod 1", "desc", 10.0, 2);
        p1.setState(StatoProdotto.APPROVATO);
        productRepo.save(p1);

        Prodotto p2 = controller.createProduct(venditore, "Prod 2", "desc", 20.0, 1);
        p2.setState(StatoProdotto.IN_ATTESA_DI_APPROVAZIONE);
        productRepo.save(p2);

        List<Prodotto> approved = controller.getApprovedProducts();
        assertEquals(1, approved.size());
        assertEquals(StatoProdotto.APPROVATO, approved.get(0).getState());
    }

    @Test
    void list_shouldReturnAllProducts() {
        controller.createProduct(venditore, "Prod 1", "desc", 10.0, 2);
        controller.createProduct(venditore, "Prod 2", "desc", 20.0, 1);

        List<Prodotto> all = controller.list();
        assertEquals(2, all.size());
    }

}
