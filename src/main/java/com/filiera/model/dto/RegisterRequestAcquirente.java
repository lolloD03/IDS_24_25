package com.filiera.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestAcquirente extends RegisterRequest {
    String città;
    String via;
    String numeroCivico;
}
