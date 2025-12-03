package com.filiera.model.events;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.filiera.model.sellers.Venditore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Invito {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    @JsonBackReference
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Venditore user;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatoInvito stato = StatoInvito.IN_ATTESA;

    private LocalDate dataInvito;

    @PrePersist
    public void prePersist() {
        dataInvito = LocalDate.now();
    }
}