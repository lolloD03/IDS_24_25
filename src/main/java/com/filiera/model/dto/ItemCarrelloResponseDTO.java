package com.filiera.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// DTO per rappresentare un singolo prodotto all'interno del carrello
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCarrelloResponseDTO {
    private UUID productId; // ID del prodotto
    private String productName; // Nome del prodotto
    private double unitPrice; // Prezzo unitario del prodotto
    private int quantity; // Quantità di questo prodotto nel carrello
    private double subtotal; // Sottototale per questo item (unitPrice * quantity)
}