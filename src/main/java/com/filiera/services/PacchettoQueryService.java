package com.filiera.services;

import com.filiera.model.dto.PacchettoResponseDTO;
import com.filiera.model.dto.ProductResponseDTO;

import java.util.List;

public interface PacchettoQueryService {
    List<PacchettoResponseDTO> getAllPacchetti();
}
