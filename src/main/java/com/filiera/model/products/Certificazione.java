package com.filiera.model.products;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificazione {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @ToString.Exclude
    @ManyToMany(mappedBy = "certificazioni")
    @JsonBackReference
    @Builder.Default
    private Set<Prodotto> prodotti = new HashSet<>();
}
