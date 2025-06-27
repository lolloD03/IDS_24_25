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


}