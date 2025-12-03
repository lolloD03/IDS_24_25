package com.filiera.model.dto;

import com.filiera.model.events.StatoInvito;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitoResponseDTO {
    private UUID invitoId;
    private StatoInvito stato;
    private LocalDate dataInvito;
    private EventoSimpleDTO evento;
}