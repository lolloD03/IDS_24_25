package com.filiera.services;

import com.filiera.exception.UserNotFoundException;
import com.filiera.model.sellers.Venditore;
import com.filiera.model.users.User;
import com.filiera.repository.InMemoryUserRepository;
import lombok.Data;


import com.filiera.model.dto.VenditoreIndirizzoDTO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MapService {

    private final InMemoryUserRepository venditoreRepo;

    public MapService(InMemoryUserRepository venditoreRepo) {
        this.venditoreRepo = venditoreRepo;
    }

    public List<VenditoreIndirizzoDTO> getVendorAddressesWithNames() {
        List<String> targetRoles = Arrays.asList("PRODUTTORE", "TRASFORMATORE", "DISTRIBUTORE");
        List<Venditore> vendors = venditoreRepo.findByRoleIn(targetRoles);

        return vendors.stream()
                .map(vendor -> {
                    VenditoreIndirizzoDTO dto = new VenditoreIndirizzoDTO();
                    dto.setNomeAzienda(vendor.getName());

                    String formattedAddress = vendor.getAddress().getRoad() + " " +
                            vendor.getAddress().getCivicNumber() + ", " +
                            vendor.getAddress().getCity();

                    dto.setIndirizzo(formattedAddress);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public String getVendorAddressById(UUID vendorId) {
        Venditore vendor = (Venditore) venditoreRepo.findById(vendorId)
                .orElseThrow(() -> new UserNotFoundException("Venditore non trovato con id: " + vendorId));

        // Ricostruisci la stringa dell'indirizzo
        String formattedAddress = vendor.getAddress().getRoad() + " " +
                vendor.getAddress().getCivicNumber() + ", " +
                vendor.getAddress().getCity();

        return formattedAddress;
    }

}