package com.filiera.model.administration;

import com.filiera.model.users.RuoloUser;
import com.filiera.model.users.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("CURATORE") // Valore specifico per il discriminatore
@NoArgsConstructor // Genera il costruttore senza argomenti, necessario per JPA
@SuperBuilder // Essenziale per estendere il builder dalla classe padre User
@ToString(callSuper = true)
public class Curatore extends User {



}
