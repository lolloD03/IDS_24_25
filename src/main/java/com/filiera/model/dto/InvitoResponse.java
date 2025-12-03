package com.filiera.model.dto;

import com.filiera.model.events.StatoInvito;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InvitoResponse {
    @NotNull
    private StatoInvito statoInvito;
}
