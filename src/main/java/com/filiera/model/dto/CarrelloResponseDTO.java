package com.filiera.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

// DTO per rappresentare l'intero carrello
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrelloResponseDTO {
    private UUID id; // ID del carrello
    private UUID buyerId; // ID dell'acquirente proprietario del carrello
    private List<ItemCarrelloResponseDTO> items; // Lista degli item nel carrello
    private double totalPrice; // Totale complessivo del carrello
}