package ModelTest;

import com.filiera.model.administration.Curatore;
import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;
import com.filiera.model.sellers.Produttore;
import com.filiera.model.sellers.Venditore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProdottoTest {

    private Prodotto prodotto;
    private Curatore curatore;

    @BeforeEach
    void setUp() {
        Venditore venditore = new Produttore(); // Stub, assicurati che abbia costruttore vuoto
        curatore = new Curatore();   // Stub, assicurati che abbia costruttore vuoto
        prodotto = new Prodotto(
                "Miele",
                "Miele biologico",
                10.5,
                15,
                venditore,
                30,
                "DOP"// giorni alla scadenza
        );
        prodotto.setState(StatoProdotto.IN_ATTESA_DI_APPROVAZIONE);
    }

    @Test
    void approveBy() {
        prodotto.approveBy(curatore);
        assertEquals(StatoProdotto.APPROVATO, prodotto.getState(), "Lo stato dovrebbe essere APPROVATO");
        assertEquals(curatore, prodotto.getApprovedBy(), "Il curatore assegnato non è corretto");
    }

    @Test
    void rejectBy() {
        prodotto.rejectBy(curatore);
        assertEquals(StatoProdotto.RIFIUTATO, prodotto.getState(), "Lo stato dovrebbe essere RIFIUTATO");
        assertEquals(curatore, prodotto.getApprovedBy(), "Il curatore assegnato non è corretto");
    }

    @Test
    void getState() {
        prodotto.setState(StatoProdotto.IN_ATTESA_DI_APPROVAZIONE);
        assertEquals(StatoProdotto.IN_ATTESA_DI_APPROVAZIONE, prodotto.getState(), "Lo stato ottenuto non è corretto");
    }

    @Test
    void setState() {
        prodotto.setState(StatoProdotto.RIFIUTATO);
        assertEquals(StatoProdotto.RIFIUTATO, prodotto.getState(), "Lo stato impostato  è RIFIUTATO");
    }

    @Test
    void getApprovedBy() {
        prodotto.setApprovedBy(curatore);
        assertEquals(curatore, prodotto.getApprovedBy(), "Il curatore ottenuto non è corretto");
    }

    @Test
    void setApprovedBy() {
        Curatore altro = new Curatore();
        prodotto.setApprovedBy(altro);
        assertEquals(altro, prodotto.getApprovedBy(), "Il curatore impostato non è corretto");
    }
}
