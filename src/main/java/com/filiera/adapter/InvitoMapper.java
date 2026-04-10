package com.filiera.adapter;

import com.filiera.model.dto.EventoSimpleDTO;
import com.filiera.model.dto.InvitoResponseDTO;
import com.filiera.model.events.Evento;
import com.filiera.model.events.Invito;
import org.springframework.stereotype.Component;

@Component
public class InvitoMapper {

    public InvitoResponseDTO toInvitoResponseDTO(Invito invito) {
        Evento evento = invito.getEvento();

        EventoSimpleDTO eventoSimpleDTO = new EventoSimpleDTO(
                evento.getId(),
                evento.getName(),
                evento.getLocation(),
                evento.getDescription(),
                evento.getEventDate());

        return new InvitoResponseDTO(
                invito.getId(),
                invito.getStato(),
                invito.getDataInvito(),
                eventoSimpleDTO);
    }

}
