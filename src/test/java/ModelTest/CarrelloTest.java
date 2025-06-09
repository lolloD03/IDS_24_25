package ModelTest;

import com.filiera.model.payment.Carrello;
import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;
import com.filiera.model.sellers.Produttore;
import com.filiera.model.users.Acquirente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarrelloTest {
    private Carrello carrello;
    private Prodotto prodotto1;
    private Prodotto prodotto2;
    private Acquirente acquirente;

    @BeforeEach
    void setUp() {
        acquirente = new Acquirente(); // Stub, crea se hai costruttore adatto
        carrello = new Carrello();
        carrello.setBuyer(acquirente);
        carrello.setProducts(new ArrayList<>());

        prodotto1 = new Prodotto(
                "Pane", "Pane integrale", 2.5, 1, new Produttore(), 5, "DOP"
        );
        prodotto2 = new Prodotto(
                "Latte", "Latte fresco", 1.2, 2, new Produttore(), 7 , "DOP"
        );
        prodotto1.setState(StatoProdotto.IN_ATTESA_DI_APPROVAZIONE);
        prodotto2.setState(StatoProdotto.IN_ATTESA_DI_APPROVAZIONE);
    }

    @Test
    void testAddProduct() {
        Prodotto added = carrello.addProduct(prodotto1);
        assertEquals(1, carrello.getProducts().size());
        assertEquals(prodotto1, added);
    }

    //TODO in questo caso il prodotto non ha stato , non approved, non rejected ecc

    @Test
    void testAddNullProductThrowsException() {
        assertThrows(RuntimeException.class, () -> carrello.addProduct(null));
    }

    @Test
    void testRemoveProduct() {
        carrello.addProduct(prodotto1);
        Prodotto removed = carrello.removeProduct(prodotto1);
        assertEquals(0, carrello.getProducts().size());
        assertEquals(prodotto1, removed);
    }

    @Test
    void testRemoveNonExistentProductThrowsException() {
        assertThrows(RuntimeException.class, () -> carrello.removeProduct(prodotto1));
    }

    @Test
    void testClearCarrello() {
        carrello.addProduct(prodotto1);
        carrello.addProduct(prodotto2);
        carrello.clearCarrello();
        assertTrue(carrello.getProducts().isEmpty());
    }

    @Test
    void testGetTotalPrice() {
        carrello.addProduct(prodotto1); // 2.5
        carrello.addProduct(prodotto2); // 1.2
        double total = carrello.getTotalPrice();
        assertEquals(3.7, total, 0.01);
    }

    @Test
    void testCarrelloIdIsNotNull() {
        assertNotNull(carrello.getId());
    }

    @Test
    void testGetSetBuyer() {
        Acquirente anotherBuyer = new Acquirente();
        carrello.setBuyer(anotherBuyer);
        assertEquals(anotherBuyer, carrello.getBuyer());
    }
}
