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
public class SellerResponseDTO {
    private UUID id;
    private String name; // Or company name, based on your Seller entity
    // Add other relevant seller details you want to expose, e.g., email
}