package ServiceTest;

import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;
import com.filiera.model.sellers.Produttore;
import com.filiera.model.sellers.Venditore;
import com.filiera.services.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductServiceImplTest {

    private InMemoryProductRepository productRepository;
    private InMemoryUserRepository userRepository;
    private ProductServiceImpl productService;

    private Venditore venditore;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        userRepository = new InMemoryUserRepository();
        productService = new ProductServiceImpl(productRepository, userRepository);

        // Venditore di prova
        venditore = new Produttore();
        venditore.setId(UUID.randomUUID());
        venditore.setEmail("venditore@example.com");
        userRepository.save(venditore);
    }

    @Test
    void createProduct_shouldSucceedWithValidSeller() {
        Prodotto p = productService.createProduct(venditore, "Pane", "Pane fresco", 2.5, 10);

        assertNotNull(p.getId());
        assertEquals("Pane", p.getName());
        assertEquals(StatoProdotto.IN_ATTESA_DI_APPROVAZIONE, p.getState());
        assertEquals(venditore, p.getSeller());
        assertEquals(1, productRepository.findAll().size());
    }

    @Test
    void createProduct_shouldFailWithUnknownSeller() {
        Venditore fake = new Produttore();
        fake.setId(UUID.randomUUID());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(fake, "Latte", "Latte bio", 1.5, 5);
        });

        assertTrue(ex.getMessage().contains("non esiste"));
    }

    @Test
    void listAll_shouldReturnAllProducts() {
        productService.createProduct(venditore, "Miele", "Miele bio", 6.0, 5);
        productService.createProduct(venditore, "Farina", "Farina tipo 0", 1.0, 15);

        List<Prodotto> list = productService.listAll();
        assertEquals(2, list.size());
    }

    @Test
    void getById_shouldReturnCorrectProduct() {
        Prodotto prodotto = productService.createProduct(venditore, "Olio", "Olio EVO", 10.0, 3);
        Optional<Prodotto> found = productService.getById(prodotto.getId());

        assertTrue(found.isPresent());
        assertEquals("Olio", found.get().getName());
    }

    @Test
    void updateProduct_shouldUpdateValues() {
        Prodotto p = productService.createProduct(venditore, "Formaggio", "Fresco", 4.0, 7);
        p.setName("Formaggio stagionato");
        p.setPrice(6.5);

        Prodotto updated = productService.updateProduct(p);

        assertEquals("Formaggio stagionato", updated.getName());
        assertEquals(6.5, updated.getPrice());
    }

    @Test
    void updateProduct_shouldThrowIfNotFound() {
        Prodotto fake = new Prodotto();
        fake.setId(UUID.randomUUID());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            productService.updateProduct(fake);
        });

        assertTrue(ex.getMessage().contains("non trovato"));
    }

    @Test
    void deleteProduct_shouldRemoveExistingProduct() {
        Prodotto p = productService.createProduct(venditore, "Burro", "Burro di malga", 3.5, 4);
        productService.deleteProduct(p);

        assertTrue(productRepository.findAll().isEmpty());
    }

    @Test
    void deleteProduct_shouldThrowIfNotFound() {
        Prodotto fake = new Prodotto();
        fake.setId(UUID.randomUUID());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            productService.deleteProduct(fake);
        });

        assertTrue(ex.getMessage().contains("non esiste"));
    }

    @Test
    void getApprovedProducts_shouldFilterByState() {
        Prodotto p1 = productService.createProduct(venditore, "Yogurt", "Yogurt greco", 2.0, 5);
        Prodotto p2 = productService.createProduct(venditore, "Uova", "Uova fresche", 3.0, 12);

        p2.setState(StatoProdotto.APPROVATO);
        productRepository.save(p2);

        List<Prodotto> approved = productService.getApprovedProducts();
        assertEquals(1, approved.size());
        assertEquals("Uova", approved.get(0).getName());
    }

}