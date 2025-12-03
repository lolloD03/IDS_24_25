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
@Table(name = "app_users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_user", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class User implements Observer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(nullable = false)
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Role cannot be null")
    private RuoloUser role;

    private boolean approved;


    @Override
    public void update(String message) {
        System.out.println("Notifica ricevuta per l'utente " + this.getName() + ": " + message);
    }

}