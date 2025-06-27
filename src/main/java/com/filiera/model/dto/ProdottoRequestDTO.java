package com.filiera.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data // Include @Getter, @Setter, @ToString, @EqualsAndHashCode
@NoArgsConstructor // Costruttore senza argomenti
@AllArgsConstructor // Costruttore con tutti gli argomenti
@Builder // Per creare facilmente istanze (utile nei test)
public class ProdottoRequestDTO {

    @NotBlank(message = "The name of the product can not be empty")
    private String name;

    @NotBlank(message = "The description of the product can not be empty")
    private String description;

    @DecimalMin(value = "0.01", message = "The price have to be greater than 0")
    private double price;

    @Min(value = 1, message = "The quantity have to be greater than 0")
    private int quantity;

    @NotBlank(message = "The certification can not be empty")
    private String certification;

    @NotNull(message = "The expiration date can not be null")
    private LocalDate expirationDate;
}