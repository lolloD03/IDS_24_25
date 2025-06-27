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
    private String process; // Puoi mantenere 'process' come nome generico, o rinominarlo a 'cultivationProcess' per chiarezza

    // I costruttori manuali sono stati rimossi grazie a @NoArgsConstructor e @SuperBuilder.
    // I metodi getCultivationProcess() e setCultivationProcess() sono stati sostituiti da @Getter e @Setter.
    // Se vuoi che i metodi Lombok generati abbiano esattamente quei nomi (getCultivationProcess/setCultivationProcess),
    // puoi rinominare il campo da 'process' a 'cultivationProcess'.
    // In alternativa, se lasci 'process', i metodi generati saranno getProcess() e setProcess().
    // Ho mantenuto 'process' come nel tuo esempio, ma sentiti libero di cambiarlo.
}