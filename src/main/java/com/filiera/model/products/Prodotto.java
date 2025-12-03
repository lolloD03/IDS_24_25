package com.filiera.model.products;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.filiera.model.administration.Curatore;
import com.filiera.model.sellers.Venditore;
import jakarta.persistence.*;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@SuperBuilder
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private StatoProdotto state;

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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Min(value = 0, message = "Price cannot be negative")
    private double price;

    @Column(nullable = false)
    @Min(value = 0, message = "Available quantity cannot be negative")
    private int availableQuantity;

    @Column(nullable = false)
    private int numeroCondivisioni = 0;


    @ManyToMany
    @JoinTable(
            name = "prodotto_certificazione",
            joinColumns = @JoinColumn(name = "prodotto_id"),
            inverseJoinColumns = @JoinColumn(name = "certificazione_id")
    )
    @JsonManagedReference
    @Builder.Default
    private Set<Certificazione> certificazioni = new HashSet<>();


    @PrePersist
    public void prePersist() {
        if (this.state == null) {
            this.state = StatoProdotto.PENDING_APPROVAL;
        }

    }

    public boolean isApproved() {
        return this.state == StatoProdotto.APPROVED;
    }

    public void addCertificazione(Certificazione cert) {
        certificazioni.add(cert);
        cert.getProdotti().add(this);
    }

    public void incrementaCondivisioni() {
        this.numeroCondivisioni++;
    }

    public void removeCertificazione(Certificazione cert) {
        certificazioni.remove(cert);
        cert.getProdotti().remove(this);
    }


    public void approveBy(Curatore curator) {
        this.setState(StatoProdotto.APPROVED);
        this.setApprovedBy(curator);
    }

    public void rejectBy(Curatore curator) {
        this.setState(StatoProdotto.REJECTED);
        this.setApprovedBy(curator);
    }


}