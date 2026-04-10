package com.filiera.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdottoUpdateDTO {

    private String name;

    private String description;

    private double price;

    private int quantity;

    private List<CertificazioneDTO> certificazioni;

    private LocalDate expirationDate;

}