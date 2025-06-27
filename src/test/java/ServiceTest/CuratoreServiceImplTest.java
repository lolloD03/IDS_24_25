package ServiceTest;

import com.filiera.model.administration.Curatore;
import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;
import com.filiera.model.users.RuoloUser;
import com.filiera.services.CuratoreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CuratoreServiceImplTest {

    private InMemoryProductRepository productRepo;
    private InMemoryUserRepository userRepo;
    private CuratoreServiceImpl service;
    private Curatore curatore;
    private Prodotto prodotto;

    @BeforeEach
    void setUp() {
        productRepo = new InMemoryProductRepository();
        userRepo = new InMemoryUserRepository();
        service = new CuratoreServiceImpl(productRepo, userRepo);

        curatore = new Curatore("pass", "curatore@example.com", "curatore1 " , RuoloUser.CURATORE);
        userRepo.save(curatore);

        prodotto = new Prodotto("Formaggio", "Pecorino stagionato", 8.0, 10, null, 60, null);
        prodotto.setState(StatoProdotto.IN_ATTESA_DI_APPROVAZIONE);
        productRepo.save(prodotto);
    }

    @Test
    void getPendingProducts_shouldReturnOnlyPending() {
        Prodotto approvato = new Prodotto("Pane", "Pane integrale", 2.0, 5, null, 3, null);
        Prodotto approvato2 = new Prodotto("Pane2", "Pane integrale", 2.0, 5, null, 3, null);

        approvato.setState(StatoProdotto.IN_ATTESA_DI_APPROVAZIONE);


        productRepo.save(approvato);


        List<Prodotto> pending = service.getPendingProducts();
        assertEquals(2, pending.size());
        assertEquals(StatoProdotto.IN_ATTESA_DI_APPROVAZIONE, pending.get(0).getState());
    }

    @Test
    void approveProduct_shouldApproveAndAssignCuratore() {
        Prodotto approved = service.approveProduct(prodotto, curatore.getId());
        assertEquals(StatoProdotto.APPROVATO, approved.getState());
        assertEquals(curatore, approved.getApprovedBy());
    }

    @Test
    void approveProduct_shouldThrowIfNotPending() {
        prodotto.setState(StatoProdotto.APPROVATO);
        productRepo.save(prodotto);

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                service.approveProduct(prodotto, curatore.getId())
        );
        assertEquals("Il prodotto non è in attesa di approvazione.", e.getMessage());
    }

    @Test
    void approveProduct_shouldThrowIfCuratoreNotFound() {
        UUID fakeId = UUID.randomUUID();
        Exception e = assertThrows(IllegalArgumentException.class, () ->
                service.approveProduct(prodotto, fakeId)
        );
        assertEquals("Il curatore con ID " + fakeId + " non esiste.", e.getMessage());
    }

    @Test
    void rejectProduct_shouldRejectAndAssignCuratore() {
        Prodotto rejected = service.rejectProduct(prodotto, curatore.getId());
        assertEquals(StatoProdotto.RIFIUTATO, rejected.getState());
        assertEquals(curatore, rejected.getApprovedBy());
    }

    @Test
    void rejectProduct_shouldThrowIfNotPending() {
        prodotto.setState(StatoProdotto.APPROVATO);
        productRepo.save(prodotto);

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                service.rejectProduct(prodotto, curatore.getId())
        );
        assertEquals("Il prodotto non è in attesa di approvazione.", e.getMessage());
    }

    @Test
    void rejectProduct_shouldThrowIfCuratoreNotFound() {
        UUID fakeId = UUID.randomUUID();
        Exception e = assertThrows(IllegalArgumentException.class, () ->
                service.rejectProduct(prodotto, fakeId)
        );
        assertEquals("Il curatore con ID " + fakeId + " non esiste.", e.getMessage());
    }

}