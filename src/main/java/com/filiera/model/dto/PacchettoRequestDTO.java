package com.filiera.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PacchettoRequestDTO {

    @NotBlank(message = "Il nome del pacchetto non può essere vuoto")
    private String name;

    private String description;

    @Positive
    private int availableQuantity;

    @Positive(message = "Il prezzo deve essere un numero positivo")
    private double price;

    @NotEmpty(message = "L'elenco dei prodotti non può essere vuoto")
    private List<UUID> productIds;
}