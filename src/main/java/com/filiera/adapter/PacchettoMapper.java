package com.filiera.adapter;

import com.filiera.model.dto.PacchettoResponseDTO;
import com.filiera.model.dto.ProductResponseDTO;
import com.filiera.model.products.Pacchetto;
import com.filiera.model.products.Prodotto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PacchettoMapper {

    public PacchettoResponseDTO toDTO(Pacchetto pacchetto) {
        if (pacchetto == null) {
            return null;
        }

        return PacchettoResponseDTO.builder()
                .id(pacchetto.getId())
                .name(pacchetto.getName())
                .description(pacchetto.getDescription())
                .price(pacchetto.getPrice())
                .quantity(pacchetto.getAvailableQuantity())
                .sellerId(pacchetto.getSeller() != null ? pacchetto.getSeller().getId() : null)
                .products(pacchetto.getProductSnapshots().stream()
                        .map(snapshot -> ProductResponseDTO.builder()
                                .name(snapshot.getName())
                                .description(snapshot.getDescription())
                                .price(snapshot.getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }


    private ProductResponseDTO toProductResponseDTO(Prodotto prodotto) {
        if (prodotto == null) {
            return null;
        }

        return ProductResponseDTO.builder()
                .id(prodotto.getId())
                .name(prodotto.getName())
                .price(prodotto.getPrice())
                .build();
    }
}