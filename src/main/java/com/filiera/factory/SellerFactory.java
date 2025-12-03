package com.filiera.factory;

import com.filiera.model.OsmMap.Indirizzo;
import com.filiera.model.dto.RegisterRequestUser;
import com.filiera.model.dto.RegisterRequestVenditore;
import com.filiera.model.sellers.DistributoreTipicita;
import com.filiera.model.sellers.Produttore;
import com.filiera.model.sellers.Trasformatore;
import com.filiera.model.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

public class SellerFactory implements UserFactory{

    PasswordEncoder passwordEncoder;


    public User createUser(RegisterRequestUser request , PasswordEncoder passwordEncoder){

        if (!(request instanceof RegisterRequestVenditore sellerRequest)) {
            throw new IllegalArgumentException("Ruolo Venditore richiede RegisterRequestVenditore.");
        }

        Indirizzo indirizzo = new Indirizzo(sellerRequest.getCittà(), sellerRequest.getVia(), sellerRequest.getNumeroCivico());


        return switch (request.getRuoloUser()) {
            case PRODUTTORE -> Produttore.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .address(indirizzo)
                    .partitaIva(sellerRequest.getPartitaIva())
                    .role(request.getRuoloUser())
                    .approved(false)
                    .build();

            case TRASFORMATORE -> Trasformatore.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .address(indirizzo)
                    .partitaIva(sellerRequest.getPartitaIva())
                    .role(request.getRuoloUser())
                    .approved(false)
                    .build();

            case DISTRIBUTORE -> DistributoreTipicita.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .address(indirizzo)
                    .partitaIva(sellerRequest.getPartitaIva())
                    .role(request.getRuoloUser())
                    .approved(false)
                    .build();

            default -> throw new IllegalArgumentException("Ruolo non valido per la registrazione venditore: " + request.getRuoloUser());
        };
    }

}
