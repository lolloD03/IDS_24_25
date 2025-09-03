package com.filiera.adapter;

import com.filiera.model.dto.ProdottoRequestDTO;
import com.filiera.model.dto.ProductResponseDTO;
import com.filiera.model.dto.SellerResponseDTO;
import com.filiera.model.products.Prodotto;
import com.filiera.model.sellers.Venditore;
import org.springframework.stereotype.Component;

@Component
public class ProdottoMapper {

    // Mapper from Entity to Response DTO (as we did before)
    public ProductResponseDTO toDTO(Prodotto prodotto) {
        if (prodotto == null) {
            return null;
        }

        SellerResponseDTO sellerDTO = null;
        if (prodotto.getSeller() != null) {
            sellerDTO = SellerResponseDTO.builder()
                    .id(prodotto.getSeller().getId())
                    .name(prodotto.getSeller().getName())
                    .build();
        }

        return ProductResponseDTO.builder()
                .id(prodotto.getId())
                .name(prodotto.getName())
                .description(prodotto.getDescription())
                .price(prodotto.getPrice())
                .availableQuantity(prodotto.getAvailableQuantity())
                .certification(prodotto.getCertification())
                .expirationDate(prodotto.getExpirationDate())
                .state(prodotto.getState().name())
                .seller(sellerDTO)
                .build();
    }

    // New mapper from Request DTO to Entity
    public Prodotto toEntity(ProdottoRequestDTO prodottoRequestDTO, Venditore seller) {
        if (prodottoRequestDTO == null) {
            return null;
        }

        return Prodotto.builder()
                .name(prodottoRequestDTO.getName())
                .description(prodottoRequestDTO.getDescription())
                .price(prodottoRequestDTO.getPrice())
                .availableQuantity(prodottoRequestDTO.getQuantity())
                .certification(prodottoRequestDTO.getCertification())
                .expirationDate(prodottoRequestDTO.getExpirationDate())
                .seller(seller) // The seller entity is passed as a separate argument
                .build();
    }
}