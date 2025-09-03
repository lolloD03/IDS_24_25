package com.filiera.adapter;

import com.filiera.model.dto.PacchettoResponseDTO;
import com.filiera.model.dto.ProductResponseDTO;
import com.filiera.model.products.Pacchetto;
import com.filiera.model.products.Prodotto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PacchettoMapper {

    /**
     * Converts a Pacchetto entity to a PacchettoResponseDTO.
     *
     * @param pacchetto The Pacchetto entity.
     * @return The corresponding PacchettoResponseDTO.
     */
    public PacchettoResponseDTO toDTO(Pacchetto pacchetto) {
        if (pacchetto == null) {
            return null;
        }

        return PacchettoResponseDTO.builder()
                .id(pacchetto.getId())
                .name(pacchetto.getName())
                .description(pacchetto.getDescription())
                .price(pacchetto.getPrice())
                .sellerId(pacchetto.getSeller() != null ? pacchetto.getSeller().getId() : null)
                .products(pacchetto.getProducts().stream()
                        .map(this::toProductResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    // Metodo helper privato per mappare l'entità Prodotto nel suo DTO
    private ProductResponseDTO toProductResponseDTO(Prodotto prodotto) {
        if (prodotto == null) {
            return null;
        }

        return ProductResponseDTO.builder()
                .id(prodotto.getId())
                .name(prodotto.getName())
                .price(prodotto.getPrice())
                // Aggiungi altri campi del prodotto se necessario
                .build();
    }
}