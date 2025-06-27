package ServiceTest;


import com.filiera.model.payment.Carrello;
import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;
import com.filiera.model.sellers.Produttore;
import com.filiera.model.sellers.Venditore;
import com.filiera.services.CarrelloServiceImpl;
import com.filiera.services.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CarrelloServiceImplTest {

    private CarrelloServiceImpl carrelloService;
    private InMemoryProductRepository productRepository;
    private InMemoryUserRepository userRepository;
    private ProductServiceImpl productService;
    private Venditore venditore;
    private Prodotto prodotto;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        userRepository = new InMemoryUserRepository();
        productService = new ProductServiceImpl(productRepository, userRepository);
        carrelloService = new CarrelloServiceImpl(productRepository, productService);

        venditore = new Produttore();
        venditore.setId(UUID.randomUUID());
        userRepository.save(venditore);

        prodotto = productService.createProduct(venditore, "Pane", "Pane fresco", 2.0, 5);
        prodotto.setState(StatoProdotto.APPROVATO); // Permettiamo l'aggiunta al carrello
        productRepository.save(prodotto);
    }

    @Test
    void addProduct_shouldAddValidProduct() {
        List<Prodotto> result = carrelloService.addProduct(prodotto);
        assertTrue(result.contains(prodotto));
    }

    @Test
    void addProduct_shouldThrowIfProductNotFound() {
        Prodotto nonEsistente = new Prodotto();
        nonEsistente.setId(UUID.randomUUID());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                carrelloService.addProduct(nonEsistente)
        );

        assertEquals("Il prodotto con id " + nonEsistente.getId() + " non esiste.", exception.getMessage());
    }

    @Test
    void removeProduct_shouldRemoveExistingProduct() {
        carrelloService.addProduct(prodotto);

        List<Prodotto> result = carrelloService.removeProduct(prodotto);
        assertFalse(result.contains(prodotto));
    }

    @Test
    void getInvoice_shouldReturnFormattedInvoice() {
        carrelloService.addProduct(prodotto);
        Carrello carrello = carrelloService.getCarrello();

        String invoice = carrelloService.getInvoice(carrello).toString();

        assertTrue(invoice.contains("Prodotto: Pane"));
        assertTrue(invoice.contains("Prezzo: 2.0"));
    }

    @Test
    void clearCarrello_shouldClearWhenNotEmpty() {
        carrelloService.addProduct(prodotto);

        assertDoesNotThrow(() -> carrelloService.clearCarrello());
        assertTrue(carrelloService.getCarrello().getProducts().isEmpty());
    }

    @Test
    void clearCarrello_shouldThrowWhenEmpty() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                carrelloService.clearCarrello()
        );

        assertEquals("Il carrello è già vuoto.", exception.getMessage());
    }

    @Test
    void getCarrello_shouldReturnInstance() {
        Carrello c = carrelloService.getCarrello();
        assertNotNull(c);
    }
}