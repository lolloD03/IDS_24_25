package com.filiera.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequestTrasformatore extends RegisterRequest{

    String città;
    String via;
    String numeroCivico;
    String partitaIva;
    String processo;

}
