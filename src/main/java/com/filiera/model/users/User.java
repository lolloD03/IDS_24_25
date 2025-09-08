package com.filiera.model.users;


import com.filiera.Observer.Observer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "app_users") // 'app_users' è un buon nome, coerenza con lo standard
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_user", discriminatorType = DiscriminatorType.STRING)
@Getter // Genera tutti i getter per tutti i campi
@Setter // Genera tutti i setter per tutti i campi
@NoArgsConstructor // Genera il costruttore senza argomenti, necessario per JPA
@SuperBuilder // Permette di usare il pattern Builder e funziona con l'ereditarietà
public abstract class User implements Observer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Delega la generazione dell'UUID a JPA/Hibernate
    private UUID id;


    @Column(nullable = false)
    @NotBlank(message = "Password cannot be blank")// Indica che la password non può essere null
    private String password;

    @Column(nullable = false, unique = true)// L'email non può essere null e deve essere unica
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be blank")// Il nome non può essere null
    private String name;

    @Enumerated(EnumType.STRING) // Memorizza l'enum come stringa nel DB, più leggibile e robusto
    @Column(nullable = false)
    @NotNull(message = "Role cannot be null")// Il ruolo non può essere null
    private RuoloUser role;


    @Override
    public void update(String message) {
        System.out.println("Notifica ricevuta per l'utente " + this.getName() + ": " + message);
    }

}