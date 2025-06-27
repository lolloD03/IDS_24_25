package com.filiera.model.payment;

import com.filiera.exception.EmptyCartException;
import com.filiera.exception.EmptyOrderException;
import com.filiera.model.users.Acquirente;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@NoArgsConstructor
@Entity
@Data
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID numeroOrdine; // UUID o codice ordine

    @ManyToOne
    @JoinColumn(name = "buyer_id" , nullable = false)
    private Acquirente buyer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOrdine> items = new ArrayList<>();

    //private double totale;

    private LocalDate dataOrdine;


    public double getTotalPrice() {
        if (items.isEmpty()) {
            throw new EmptyOrderException("Order is empty");
        }
        return items.stream().mapToDouble(ItemOrdine::getTotal).sum();
    }


    public void addItem(ItemOrdine item) {
        this.items.add(item);
        item.setOrder(this);
    }

    public void removeItem(ItemOrdine item) {
        this.items.remove(item);
        item.setOrder(null);
    }
}