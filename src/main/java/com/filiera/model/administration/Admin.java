package com.filiera.model.administration;

import com.filiera.model.users.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("ADMIN")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Admin extends User {

}