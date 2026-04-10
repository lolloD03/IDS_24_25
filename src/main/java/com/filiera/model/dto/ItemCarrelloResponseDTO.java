package com.filiera.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCarrelloResponseDTO {
    private UUID productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double subtotal;
}