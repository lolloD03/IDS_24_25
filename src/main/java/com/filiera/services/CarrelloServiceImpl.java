package com.filiera.services;

import com.filiera.exception.BuyerNotFoundException;
import com.filiera.exception.EmptyCartException;
import com.filiera.exception.ProductNotFoundException;
import com.filiera.adapter.CarrelloOrdineMapper;
import com.filiera.model.dto.CarrelloResponseDTO;
import com.filiera.model.dto.OrdineResponseDTO;
import com.filiera.model.payment.Carrello;
import com.filiera.model.payment.ItemCarrello;
import com.filiera.model.payment.ItemOrdine;
import com.filiera.model.products.Prodotto;
import com.filiera.model.users.Acquirente;
import com.filiera.repository.InMemoryCarrelloRepository;
import com.filiera.repository.InMemoryOrdineRepository;
import com.filiera.repository.InMemoryUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.filiera.model.payment.Ordine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class CarrelloServiceImpl {

    private final ProductServiceImpl productService;

    private final InMemoryCarrelloRepository cartRepo;

    private final InMemoryUserRepository buyerRepo;

    private final InMemoryOrdineRepository ordineRepo;

    private final PacchettoService pacchettoService;

    private final CarrelloOrdineMapper  mapper;

    public CarrelloServiceImpl(ProductServiceImpl productService , InMemoryCarrelloRepository cartRepo, InMemoryUserRepository buyerRepo, InMemoryOrdineRepository ordineRepo,  CarrelloOrdineMapper mapper,  PacchettoService pacchettoService ) {
        this.cartRepo = cartRepo;
        this.productService = productService;
        this.buyerRepo = buyerRepo;
        this.ordineRepo = ordineRepo;
        this.mapper = mapper;
        this.pacchettoService = pacchettoService;
    }


    public CarrelloResponseDTO addProduct(UUID productId , int quantity , UUID buyerId) {


        Prodotto product = productService.getByIdEntity(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with Id " + productId + " doesn't exist."));

        Carrello carrello = getCartEntity(buyerId);

        carrello.addProduct(product , quantity);

        cartRepo.save(carrello);
        return mapper.toCarrelloResponseDTO(carrello);
    }

    public CarrelloResponseDTO removeProduct(UUID productId , int quantity , UUID buyerId) {
        Prodotto product = productService.getByIdEntity(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with Id " + productId + " doesn't exist."));
// ...

        Carrello cart = getCartEntity(buyerId);

        if(cartIsEmpty(buyerId)) {
            throw new EmptyCartException("Cart is empty");
        }

        cart.removeProduct(product, quantity);

        cartRepo.save(cart);
        return mapper.toCarrelloResponseDTO(cart);
    }


    public void clearCart(UUID buyerId) {

        Carrello carrello = getCartEntity(buyerId);

        if(cartIsEmpty(buyerId)) {
            throw new EmptyCartException("CartIsEmpty");
        }

        carrello.clearCarrello();
        cartRepo.save(carrello);
    }

    public Carrello loadOrCreateCart(UUID buyerId)  {

        Acquirente buyer = (Acquirente) buyerRepo.findById(buyerId)
                .orElseThrow(() -> new BuyerNotFoundException("Buyer not found!"));

        return cartRepo.findByBuyerId(buyerId)
                .orElseGet(() -> {
                    Carrello newCart = new Carrello();
                    newCart.setBuyer(buyer);
                    return cartRepo.save(newCart);
                });
    }

    // Metodo helper per ottenere l'entità Carrello (rinominato per chiarezza)
    public Carrello getCartEntity(UUID buyerId) {
        return loadOrCreateCart(buyerId);
    }

    public CarrelloResponseDTO getCart(UUID buyerId)  {
        Carrello cart = getCartEntity(buyerId);
        return  mapper.toCarrelloResponseDTO(cart);
    }

    public OrdineResponseDTO buyCart(UUID buyerId) {

        Carrello cart = getCartEntity(buyerId);

        List<ItemCarrello > listOfItems = cart.getProducts();

        if(cartIsEmpty(buyerId)) {
            throw new EmptyCartException("Cart is empty");
        }

        for (ItemCarrello item : cart.getProducts()) {
            if (item.getProduct() != null) {
                productService.decreaseQuantity(item.getProduct().getId(), item.getQuantity());
            } else if (item.getPacchetto() != null) {
                pacchettoService.decreaseQuantity(item.getPacchetto().getId(), item.getQuantity());
            } else {
                throw new IllegalStateException("Articolo nel carrello non valido.");
            }
        }

        Ordine order = new Ordine();
        order.setBuyer((Acquirente) buyerRepo.findById(buyerId)
                .orElseThrow(() -> new BuyerNotFoundException("Buyer not found!")));
        order.setDataOrdine(LocalDate.now());
        ordineRepo.save(order);


        List<ItemOrdine> orderItems = new ArrayList<>();
        double totalOrderPrice = 0.0;

        for (ItemCarrello item : cart.getProducts()) {
            ItemOrdine itemOrdine = new ItemOrdine();
            if (item.getProduct() != null) {
                itemOrdine.setProductId(item.getProduct().getId());
                itemOrdine.setProductName(item.getProduct().getName());
                itemOrdine.setProductPrice(item.getProduct().getPrice());
                itemOrdine.setQuantity(item.getQuantity());
                totalOrderPrice += item.getProduct().getPrice() * item.getQuantity();
            } else { // Deve essere un pacchetto
                itemOrdine.setProductId(item.getPacchetto().getId());
                itemOrdine.setProductName(item.getPacchetto().getName());
                itemOrdine.setProductPrice(item.getPacchetto().getPrice());
                itemOrdine.setQuantity(item.getQuantity());
                totalOrderPrice += item.getPacchetto().getPrice() * item.getQuantity();
            }
            itemOrdine.setOrder(order); // Imposta la relazione bidirezionale
            orderItems.add(itemOrdine);
        }

        order.setItems(orderItems);



        clearCart(buyerId);
        return mapper.toOrdineResponseDTO(order);
    }


    public boolean cartIsEmpty(UUID buyerId) {
        Carrello cart = getCartEntity(buyerId);
        return cart.getProducts().isEmpty();
    }
}
