package com.filiera.model.users;

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
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Delega la generazione dell'UUID a JPA/Hibernate
    // Non è più necessario specificare columnDefinition per H2,
    // dato che supporta il tipo UUID nativo.
    private UUID id;

    // Aggiungiamo alcune validazioni base con Jakarta Validation (JSR 380)
    // Richiede la dipendenza 'jakarta.validation:jakarta.validation-api' e un'implementazione come 'org.hibernate.validator:hibernate-validator'
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

    // Se hai bisogno di un costruttore specifico che non sia un "no-args"
    // e che NON generi l'ID manualmente (perché lo fa @GeneratedValue),
    // puoi crearlo così. @SuperBuilder è spesso una buona alternativa a costruttori multipli.
    // Esempio:
    // public User(String password, String email, String name, RuoloUser ruolo) {
    //     this.password = password;
    //     this.email = email;
    //     this.name = name;
    //     this.ruolo = ruolo;
    // }
    // Nota: Se usi @SuperBuilder e vuoi comunque un costruttore con tutti gli argomenti (tranne l'ID),
    // potresti considerare @AllArgsConstructor(exclude = "id") se non usi il costruttore manuale sopra.
    // Tuttavia, per entità con ID generato automaticamente, il builder è spesso il modo più pulito
    // per creare nuove istanze senza preoccuparsi dell'ID iniziale.
}