package com.filiera.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class EventoRequestDTO {

    @NotBlank
    private String name;
    private String description;

    @NotNull
    private LocalDate eventDate;

    @NotBlank
    private String location;

}