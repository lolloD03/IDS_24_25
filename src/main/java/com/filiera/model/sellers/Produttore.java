package com.filiera.model.sellers;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("PRODUTTORE") // Valore specifico per il discriminatore
@Getter // Genera i getter per 'process'
@Setter // Genera i setter per 'process'
@NoArgsConstructor // Genera il costruttore senza argomenti, necessario per JPA
@SuperBuilder // Essenziale per estendere il builder dalla classe padre Venditore
@ToString(callSuper = true) // Genera un toString che include anche i campi delle classi padre (Venditore, User)
public class Produttore extends Venditore {

    @Column(nullable = false) // Assicuriamo che il processo di coltivazione/produzione non sia null
    private String process;

}