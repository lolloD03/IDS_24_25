package com.filiera.model.sellers;



import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.UUID;
@Entity
@DiscriminatorValue("TRASFORMATORE") // Valore specifico per il discriminatore
@Getter // Genera i getter per 'process'
@Setter // Genera i setter per 'process'
@NoArgsConstructor // Genera il costruttore senza argomenti, necessario per JPA
@SuperBuilder // Essenziale per estendere il builder dalla classe padre Venditore
@ToString(callSuper = true) // Genera un toString che include anche i campi delle classi padre (Venditore, User)
public class Trasformatore extends Venditore {

    @Column(nullable = false) // Assicuriamo che il processo di produzione non sia null
    private String process;

    // Con @SuperBuilder, @NoArgsConstructor e @Getter/@Setter,
    // i costruttori e i metodi getter/setter manuali non sono più necessari.
    // Il campo 'process' è ora gestito dalle annotazioni Lombok.
    // L'ID e la PartitaIVA sono gestiti dalle classi User e Venditore.

    // Il costruttore manuale precedente:
    // public Trasformatore(String password, String email, String name, RuoloUser ruoloUser, int partitaIva) {
    //     super(password, email, name, ruoloUser, partitaIva);
    //     this.process = process; // Attenzione: qui 'process' non era inizializzato correttamente!
    // }
    // Non è più necessario grazie a @SuperBuilder.
}



