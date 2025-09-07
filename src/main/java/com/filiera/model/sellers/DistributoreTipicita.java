package com.filiera.model.sellers;

import com.filiera.model.products.Pacchetto;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@Entity
@DiscriminatorValue("DISTRIBUTORE")
@Getter
@Setter
public class DistributoreTipicita extends Venditore {

    @OneToMany(mappedBy = "seller")
    @Builder.Default // <-- Important: Tells the builder to use this default initialization
    private List<Pacchetto> pacchetti = new ArrayList<>();



}

