package com.filiera.model.users;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("ACQUIRENTE")
@Getter // Genera i getter per i campi di Acquirente
@Setter // Genera i setter per i campi di Acquirente
@NoArgsConstructor // Genera il costruttore senza argomenti, necessario per JPA
@SuperBuilder // Permette di usare il pattern Builder e funziona con l'ereditarietà
@ToString(callSuper = true) // Genera un toString che include anche i campi della classe padre
public class Acquirente extends User {

    // Con @SuperBuilder, @NoArgsConstructor e @Getter/@Setter,
    // i costruttori e i metodi toString manuali non sono più necessari.
    // L'ID è ora gestito dalla classe User tramite @GeneratedValue.

    // Il tuo costruttore precedente:
    // public Acquirente( String password, String email, String name , RuoloUser ruoloUser, String surname) {
    //     super( password, email, name , ruoloUser); // Qui passavi i campi a super, ma ora la generazione ID è automatica
    // }
    // Non è più necessario, useremo il builder.

    // Anche il metodo toString() manuale è stato sostituito da @ToString(callSuper = true)
}