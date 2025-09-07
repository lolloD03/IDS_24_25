package com.filiera.model.events;

import com.filiera.Observer.Observer;
import com.filiera.Observer.Subject;
import com.filiera.model.users.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class Evento implements Subject {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String name;
    private String description;
    private LocalDate eventDate;
    private String location;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private AnimatoreFiliera organizer;

    // Relazione molti-a-molti con tutti gli utenti invitati
    @ManyToMany
    @JoinTable(
            name = "evento_invitati",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private Set<User> invitedUsers = new HashSet<>();

    @Transient // This field is not persisted in the database
    private List<Observer> observers = new ArrayList<>();

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        String message = "Sei stato invitato all'evento: " + this.getName();
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}