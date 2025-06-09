package com.filiera.model.products;

import com.filiera.model.Curatore;
import com.filiera.model.sellers.Venditore;

import java.time.LocalDate;
import java.util.UUID;

public class Prodotto {

    private UUID id;
    private String name;
    private String description;
    private double price;
    private int availableQuantity;
    private StatoProdotto state;
    private Venditore seller;
    private Curatore approvedBy;
    private LocalDate expirationDate;
    private String certification;

    public Prodotto() {
        // Default constructor
    }

    public Prodotto(String name, String description, double price, int quantity, Venditore seller,int daysToExpire,String certification) {
        this.certification = certification;
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableQuantity = quantity;
        this.seller =seller;
        this.setExpirationDate(LocalDate.now().plusDays(daysToExpire));
    }

    public static Prodotto creaProdotto(String name, String description, double price, int quantity, Venditore seller,String certification) {
        Prodotto prodotto = new Prodotto();
        prodotto.certification = certification;
        prodotto.id = UUID.randomUUID();
        prodotto.name = name;
        prodotto.description = description;
        prodotto.price = price;
        prodotto.availableQuantity = quantity;
        prodotto.seller = seller;
        prodotto.state = StatoProdotto.IN_ATTESA_DI_APPROVAZIONE;
        return prodotto;
    }

    public void aggiornaProdotto(String name, String description, double price, int quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableQuantity = quantity;
    }

    public void approveBy(Curatore curatore) {
        this.state = StatoProdotto.APPROVATO;
        this.approvedBy = curatore;
    }

    public void rejectBy(Curatore curatoreObj) {
        this.state = StatoProdotto.RIFIUTATO;
        this.approvedBy = curatoreObj;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public StatoProdotto getState() {
        return state;
    }

    public void setState(StatoProdotto state) {
        this.state = state;
    }

    public Venditore getSeller() {
        return seller;
    }

    public void setSeller(Venditore seller) {
        this.seller = seller;
    }

    public Curatore getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Curatore approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public String toString(){

        return "Prodotto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", availableQuantity=" + availableQuantity +
                ", state=" + state +
                ", seller=" + seller +
                ", approvedBy=" + approvedBy +
                ", expirationDate=" + expirationDate +
                ", certification='" + certification + '\'' +
                '}';
    }
}
