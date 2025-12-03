package com.filiera.services;

import com.filiera.model.dto.EventoSimpleDTO;

import java.util.List;
import java.util.UUID;

public interface EventQueryService {
    public List<EventoSimpleDTO> getEventsDTO();
    public List<EventoSimpleDTO> getEventsRegisteredByUser(UUID buyerId);
}
