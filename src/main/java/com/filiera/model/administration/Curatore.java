package com.filiera.model.administration;

import com.filiera.model.users.RuoloUser;
import com.filiera.model.users.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("CURATORE")
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Curatore extends User {



}
