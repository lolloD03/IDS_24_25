package com.filiera.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoSimpleDTO {
    private UUID eventoId;
    private String name;
    private String location;
    private String description;
    private LocalDate eventDate;
}