package ControllerTest;

import com.filiera.controller.CarrelloController;
import com.filiera.model.payment.Carrello;
import com.filiera.model.products.Prodotto;
import com.filiera.repository.InMemoryProductRepository;
import com.filiera.repository.InMemoryUserRepository;
import com.filiera.services.CarrelloServiceImpl;
import com.filiera.services.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarrelloControllerTest {

    private CarrelloServiceImpl service;
    private CarrelloController controller;
    private InMemoryProductRepository productRepository;
    private ProductServiceImpl productService;

    private Prodotto prodotto;

    @BeforeEach
    void setUp() {
        productRepository = new InMemoryProductRepository();
        productService = new ProductServiceImpl(productRepository, new InMemoryUserRepository());
        service = new CarrelloServiceImpl(productRepository, productService);
        controller = new CarrelloController(service);

        prodotto = new Prodotto("Pane", "Pane fresco", 2.0, 1, null, 10,"DOP");
        productRepository.save(prodotto); // assicurati che esista nel repository
    }

    @Test
    void addToCart_shouldAddProduct() {
        Carrello result = controller.addToCart(prodotto);
        assertNotNull(result);
        assertTrue(service.getCarrello().getProducts().contains(prodotto));
    }

    @Test
    void addToCart_shouldReturnNullIfProductIsNull() {
        Carrello result = controller.addToCart(null);
        assertNull(result);
    }

    @Test
    void removeFromCart_shouldRemoveProduct() {
        controller.addToCart(prodotto);
        Carrello result = controller.removeFromCart(prodotto);
        assertNotNull(result);
        assertFalse(service.getCarrello().getProducts().contains(prodotto));
    }

    @Test
    void removeFromCart_shouldReturnNullIfProductIsNull() {
        assertNull(controller.removeFromCart(null));
    }

    @Test
    void getInvoice_shouldReturnInvoice() {
        controller.addToCart(prodotto);
        StringBuilder invoice = controller.getInvoice(service.getCarrello());
        assertNotNull(invoice);
        assertTrue(invoice.toString().contains("Fattura"));
    }

    @Test
    void getInvoice_shouldReturnNullIfCartIsEmpty() {
        StringBuilder invoice = controller.getInvoice(service.getCarrello());
        assertNull(invoice);
    }

    @Test
    void getInvoice_shouldReturnNullIfCartIsNull() {
        assertNull(controller.getInvoice(null));
    }

    @Test
    void getCart_shouldReturnCart() {
        Carrello c = controller.getCart();
        assertNotNull(c);
    }

}
