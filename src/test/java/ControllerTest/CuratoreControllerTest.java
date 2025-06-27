package ControllerTest;


import com.filiera.controller.CuratoreController;
import com.filiera.model.administration.Curatore;
import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;
import com.filiera.services.CuratoreServiceImpl;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class CuratoreControllerTest {

    private InMemoryProductRepository productRepo;
    private InMemoryUserRepository userRepo;
    private CuratoreServiceImpl service;
    private CuratoreController controller;

    private Curatore curatore;
    private Prodotto prodotto;

    @BeforeEach
    void setUp() {
        productRepo = new InMemoryProductRepository();
        userRepo = new InMemoryUserRepository();
        service = new CuratoreServiceImpl(productRepo, userRepo);
        controller = new CuratoreController(service);

        // Set up a test curator and product
        curatore = new Curatore();
        curatore.setId(UUID.randomUUID());
        userRepo.save(curatore);

        prodotto = new Prodotto();
        prodotto.setId(UUID.randomUUID());
        prodotto.setName("Prodotto Test");
        prodotto.setState(StatoProdotto.IN_ATTESA_DI_APPROVAZIONE);
        productRepo.save(prodotto);
    }

    @Test
    void getPendingProducts_shouldReturnCorrectList() {
        List<Prodotto> result = controller.getPendingProducts();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(prodotto.getId(), result.get(0).getId());
    }

    @Test
    void approveProduct_shouldChangeState() {
        Prodotto result = controller.approveProduct(prodotto, curatore.getId());
        assertNotNull(result);
        assertEquals(StatoProdotto.APPROVATO, result.getState());
    }

    @Test
    void rejectProduct_shouldChangeState() {
        Prodotto result = controller.rejectProduct(prodotto, curatore.getId());
        assertNotNull(result);
        assertEquals(StatoProdotto.RIFIUTATO, result.getState());
    }

    @Test
    void getPendingProducts_shouldReturnEmptyListIfNonePending() {
        prodotto.setState(StatoProdotto.APPROVATO);
        productRepo.save(prodotto);

        List<Prodotto> result = controller.getPendingProducts();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void approveProduct_shouldRemoveFromPendingList() {
        // Controlla che il prodotto sia inizialmente nei pending
        List<Prodotto> pendingBefore = controller.getPendingProducts();
        assertTrue(pendingBefore.contains(prodotto));

        // Approvazione
        controller.approveProduct(prodotto, curatore.getId());

        // Il prodotto NON dovrebbe più essere pending
        List<Prodotto> pendingAfter = controller.getPendingProducts();
        assertFalse(pendingAfter.contains(prodotto));
    }

    @Test
    void rejectProduct_shouldRemoveFromPendingList() {
        // Controlla che il prodotto sia inizialmente pending
        List<Prodotto> pendingBefore = controller.getPendingProducts();
        assertTrue(pendingBefore.contains(prodotto));

        // Rifiuto
        controller.rejectProduct(prodotto, curatore.getId());

        // Il prodotto NON dovrebbe più essere pending
        List<Prodotto> pendingAfter = controller.getPendingProducts();
        assertFalse(pendingAfter.contains(prodotto));
    }

}
