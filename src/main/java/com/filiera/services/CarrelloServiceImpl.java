package com.filiera.services;

import com.filiera.exception.BuyerNotFoundException;
import com.filiera.exception.EmptyCartException;
import com.filiera.exception.ProductNotFoundException;
import com.filiera.model.dto.CarrelloResponseDTO;
import com.filiera.model.dto.ItemCarrelloResponseDTO;
import com.filiera.model.dto.OrdineResponseDTO;
import com.filiera.model.payment.Carrello;
import com.filiera.model.payment.ItemCarrello;
import com.filiera.model.payment.ItemOrdine;
import com.filiera.model.products.Prodotto;
import com.filiera.model.users.Acquirente;
import com.filiera.repository.InMemoryCarrelloRepository;
import com.filiera.repository.InMemoryOrdineRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.filiera.model.payment.Ordine;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
public class CarrelloServiceImpl {

    private final ProductService productService;

    private final InMemoryCarrelloRepository cartRepo;

    private final InMemoryAcquirenteRepository buyerRepo;

    private final InMemoryOrdineRepository ordineRepo;

    public CarrelloServiceImpl(ProductService productService , InMemoryCarrelloRepository cartRepo, InMemoryAcquirenteRepository buyerRepo, InMemoryOrdineRepository ordineRepo) {
        this.cartRepo = cartRepo;
        this.productService = productService;
        this.buyerRepo = buyerRepo;
        this.ordineRepo = ordineRepo;
    }


    // Metodo helper per mappare l'entità Carrello al DTO di risposta
    private CarrelloResponseDTO mapToCartResponseDTO(Carrello cart) {
        if (cart == null) {
            return null;
        }

        List<ItemCarrelloResponseDTO> itemDTOs = cart.getProducts().stream()
                .map(item -> ItemCarrelloResponseDTO.builder()
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .unitPrice(item.getProduct().getPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getProduct().getPrice() * item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return CarrelloResponseDTO.builder()
                .id(cart.getId())
                .buyerId(cart.getBuyer().getId())
                .items(itemDTOs)
                .totalPrice(cart.getTotalPrice()) // Assumendo che getTotalPrice() calcoli dinamicamente
                .build();
    }

    // Metodo helper per mappare l'entità Ordine al DTO di risposta
    private OrdineResponseDTO mapToOrderResponseDTO(Ordine order) {
        if (order == null) {
            return null;
        }

        List<ItemCarrelloResponseDTO> itemDTOs = order.getItems().stream() // Assumendo che Ordine.getItems() esista
                .map(item -> ItemCarrelloResponseDTO.builder()
                        .productId(item.getProductId()) // L'ID del prodotto è già in ItemOrdine
                        .productName(item.getProductName())
                        .unitPrice(item.getProductPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getProductPrice() * item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return OrdineResponseDTO.builder()
                .id(order.getNumeroOrdine())
                .buyerId(order.getBuyer().getId()) // Assumendo che Ordine abbia un riferimento all'Acquirente
                .orderDate(order.getDataOrdine()) // Assumendo che Ordine abbia getDataOrdine()
                .totalAmount(order.getTotalPrice())    // Assumendo che Ordine.getTotale() sia calcolato o persistito
                .items(itemDTOs)
                .build();
    }

    public CarrelloResponseDTO addProduct(UUID productId , int quantity , UUID buyerId) {


        Prodotto product = productService.getByIdEntity(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with Id " + productId + " doesn't exist."));

        Carrello carrello = getCartEntity(buyerId);

        carrello.addProduct(product , quantity);

        cartRepo.save(carrello);
        return mapToCartResponseDTO(carrello);
    }

    public CarrelloResponseDTO removeProduct(UUID productId , int quantity , UUID buyerId) {
        Prodotto product = productService.getByIdEntity(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with Id " + productId + " doesn't exist."));

        Carrello cart = getCartEntity(buyerId);

        if(cartIsEmpty(buyerId)) {
            throw new EmptyCartException("Cart is empty");
        }

        cart.removeProduct(product, quantity);

        cartRepo.save(cart);
        return mapToCartResponseDTO(cart);
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

        Acquirente buyer = buyerRepo.findById(buyerId)
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
        return  mapToCartResponseDTO(cart);
    }

    public OrdineResponseDTO buyCart(UUID buyerId) {

        Carrello cart = getCartEntity(buyerId);

        List<ItemCarrello > listOfItems = cart.getProducts();

        if(cartIsEmpty(buyerId)) {
            throw new EmptyCartException("Cart is empty");
        }

        for (ItemCarrello item : cart.getProducts()) {
            Prodotto product = productService.getByIdEntity(item.getProduct().getId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found!"));


            productService.decreaseQuantity(product.getId(), item.getQuantity());
        }

        Ordine order = new Ordine();
        order.setBuyer(buyerRepo.findById(buyerId)
                .orElseThrow(() -> new BuyerNotFoundException("Buyer not found!")));
        order.setDataOrdine(LocalDate.now());
        ordineRepo.save(order);

        List<ItemOrdine> orderItems = listOfItems.stream()
                .map(item -> new ItemOrdine(item.getIdItem() ,item.getProduct().getName(), item.getProduct().getPrice() ,item.getQuantity()))
                .toList();


        for (ItemOrdine itemOrdine : orderItems) {
            order.addItem(itemOrdine);
        }



        //ordine.setTotale(carrello.getTotalPrice());

        clearCart(buyerId);
        return mapToOrderResponseDTO(order);
    }


    public boolean cartIsEmpty(UUID buyerId) {
        Carrello cart = getCartEntity(buyerId);
        return cart.getProducts().isEmpty();
    }
}
