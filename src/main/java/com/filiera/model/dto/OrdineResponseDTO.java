package com.filiera.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdineResponseDTO {
    private UUID id;
    private UUID buyerId; // L'ID dell'acquirente che ha fatto l'ordine
    private LocalDate orderDate; // Data dell'ordine
    private double totalAmount; // Importo totale dell'ordine
    private List<ItemCarrelloResponseDTO> items; // Lista degli articoli nell'ordine

    // Potresti voler aggiungere anche un OrderStatus se hai uno stato per l'ordine
    // private StatoOrdine status;
}