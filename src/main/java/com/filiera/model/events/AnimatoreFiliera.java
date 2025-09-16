package com.filiera.model.events;

import com.filiera.model.users.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@DiscriminatorValue("ANIMATORE")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AnimatoreFiliera extends User {

    // Lista degli eventi organizzati da questo animatore
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Evento> organizedEvents = new HashSet<>();

}