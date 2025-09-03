package com.filiera.services;

import com.filiera.model.dto.ProdottoRequestDTO;
import com.filiera.model.dto.ProductResponseDTO;
import com.filiera.model.products.Prodotto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface ProductService {
    //Prodotto createProduct(UUID seller, String name, String descrizione, double price, int quantity, String certitfication);
    List<ProductResponseDTO> listAll();
    Optional<ProductResponseDTO> getById(UUID id);
    Optional<Prodotto> getByIdEntity(UUID id);

    ProductResponseDTO createProduct(ProdottoRequestDTO prodottoRequestDTO, UUID venditoreId);

    ProductResponseDTO updateProduct(UUID prodottoId, ProdottoRequestDTO prodottoRequestDTO , UUID venditoreId);

    void deleteProduct(UUID prodottoId);

    public List<ProductResponseDTO> getApprovedProducts();


    void decreaseQuantity(UUID prodottoId, int quantity);

    Prodotto checkProductState(UUID prod);
}
