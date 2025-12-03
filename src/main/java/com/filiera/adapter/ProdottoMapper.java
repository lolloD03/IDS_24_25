package com.filiera.adapter;

import com.filiera.model.dto.*;
import com.filiera.model.products.Certificazione;
import com.filiera.model.products.Prodotto;
import com.filiera.model.sellers.Venditore;
import com.filiera.repository.InMemoryCertificazioneRepository;
import org.springframework.stereotype.Component;

@Component
public class ProdottoMapper {


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
                .certification(prodotto.getCertificazioni().toString())
                .expirationDate(prodotto.getExpirationDate())
                .state(prodotto.getState().name())
                .seller(sellerDTO)
                .build();
    }



    public Prodotto toEntity(ProdottoRequestDTO prodottoRequestDTO, Venditore seller) {
        if (prodottoRequestDTO == null) {
            return null;
        }

        Prodotto prodotto = Prodotto.builder()
                .name(prodottoRequestDTO.getName())
                .description(prodottoRequestDTO.getDescription())
                .price(prodottoRequestDTO.getPrice())
                .availableQuantity(prodottoRequestDTO.getQuantity())
                .expirationDate(prodottoRequestDTO.getExpirationDate())
                .seller(seller)
                .build();

        return prodotto;
    }

    public void updateEntity(Prodotto prodotto, ProdottoUpdateDTO dto) {
        if (dto == null || prodotto == null) {
            return;
        }



        if (dto.getName() != null) {
            prodotto.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            prodotto.setDescription(dto.getDescription());
        }

        if (dto.getPrice() >= 0) {
            prodotto.setPrice(dto.getPrice());
        }

        if (dto.getQuantity() >= 0) {
            prodotto.setAvailableQuantity(dto.getQuantity());
        }

        if (dto.getExpirationDate() != null) {
            prodotto.setExpirationDate(dto.getExpirationDate());
        }

    }

}