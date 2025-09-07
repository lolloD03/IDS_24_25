package com.filiera.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PacchettoResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private double price;
    private UUID sellerId;
    private List<ProductResponseDTO> products; // Un elenco di DTO dei prodotti
}
