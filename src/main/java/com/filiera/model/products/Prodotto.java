package com.filiera.model.products;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.filiera.model.administration.Curatore;
import com.filiera.model.sellers.Venditore;
import jakarta.persistence.*;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;
@Entity
// @DiscriminatorColumn non è necessario su una classe che non è parte di una gerarchia di ereditarietà JPA.
// Se Prodotto fosse classe base per altri tipi di prodotti (es. ProdottoAgricolo, ProdottoArtigianale),
// allora avrebbe senso. Ma in base al tuo schema, Prodotto è una classe concreta.
@Data // Genera getter, setter, equals, hashCode, toString
@NoArgsConstructor // Costruttore senza argomenti per JPA
// Abilita il pattern Builder per una creazione oggetti fluida
@EqualsAndHashCode(of = "id") // Genera equals/hashCode solo basato sull'ID per le entità
@ToString // Genera toString
@SuperBuilder
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING) // Mappa l'enum come stringa nel DB
    private StatoProdotto state; // Enum StatoProdotto (APPROVATO, ESAURITO, IN_ATTESA_DI_APPROVAZIONE, RIFIUTATO)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    @JsonBackReference
    private Venditore seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_id")
    private Curatore approvedBy;


    @FutureOrPresent(message = "Expiration date cannot be in the past")
    private LocalDate expirationDate;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT") // Utile per testi più lunghi
    private String description;

    @Column(nullable = false)
    @Min(value = 0, message = "Price cannot be negative") // Assicura che il prezzo non sia negativo
    private double price;

    @Column(nullable = false)
    @Min(value = 0, message = "Available quantity cannot be negative")
    @Version
    private int availableQuantity;

    private String certification; // Potrebbe essere nullable = true per default

    // --- Lifecycle Callbacks JPA ---
    // Questo metodo viene eseguito prima che l'entità venga salvata per la prima volta
    @PrePersist
    public void prePersist() {
        if (this.state == null) {
            this.state = StatoProdotto.PENDING_APPROVAL;
        }

    }

    // --- Metodi con Logica di Business ---
    // Nota: I getter e setter standard sono forniti da @Data


    public void approveBy(Curatore curator) {
        this.setState(StatoProdotto.APPROVED);
        this.setApprovedBy(curator);
    }

    public void rejectBy(Curatore curator) {
        this.setState(StatoProdotto.REJECTED);
        this.setApprovedBy(curator);
    }
}