package com.filiera.services;

import com.filiera.model.dto.ProdottoRequestDTO;
import com.filiera.model.dto.ProductResponseDTO;
import com.filiera.model.products.Prodotto;

import java.util.List;
import java.util.UUID;

public interface ProdottoQueryService {
    public List<ProductResponseDTO> getApprovedProducts();
    public void condividiProdotto(UUID idProdotto);
}
