package com.filiera.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {
    private UUID id;
    private String name;
    private String description; // Use consistent naming: 'description'
    private double price;
    private int availableQuantity;
    private String certification;
    private LocalDate expirationDate;
    private String state; // Represents StatoProdotto as a String (e.g., "APPROVED")
    private SellerResponseDTO seller; // Nested DTO for seller information
    // Add other fields you want to expose, but keep it concise
}
