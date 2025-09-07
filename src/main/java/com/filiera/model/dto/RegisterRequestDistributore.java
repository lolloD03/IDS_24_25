package com.filiera.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequestDistributore extends RegisterRequest {
    private String città;
    private String via;
    private String numeroCivico;
    private String partitaIva;
}