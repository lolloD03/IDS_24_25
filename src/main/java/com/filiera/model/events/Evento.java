package com.filiera.model.events;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.filiera.Observer.Observer;
import com.filiera.Observer.Subject;
import com.filiera.model.users.Acquirente;
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
    @JsonManagedReference
    private AnimatoreFiliera organizer;


    @ManyToMany
    @JoinTable(
            name = "evento_partecipanti",
            joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "acquirente_id")
    )
    @Builder.Default
    private Set<Acquirente> registeredBuyers = new HashSet<>();

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true) // Eager può essere utile qui
    @Builder.Default
    private Set<Invito> inviti = new HashSet<>();

    @Transient
    @Builder.Default
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

    public void addInvito(Invito invito) {
        inviti.add(invito);
    }

    public void registerBuyer(Acquirente acquirente) {
        if (registeredBuyers.contains(acquirente)) {
            throw new IllegalStateException("L'acquirente ha già prenotato questo evento.");
        }
        registeredBuyers.add(acquirente);
    }

    public void removeBuyer(Acquirente acquirente) {
        if (!registeredBuyers.contains(acquirente)) {
            throw new IllegalStateException("L'acquirente ha già prenotato questo evento.");
        }
        registeredBuyers.remove(acquirente);
    }


}