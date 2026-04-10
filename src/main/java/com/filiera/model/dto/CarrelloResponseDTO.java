package com.filiera.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrelloResponseDTO {
    private UUID id;
    private UUID buyerId;
    private List<ItemCarrelloResponseDTO> items;
    private double totalPrice;
}