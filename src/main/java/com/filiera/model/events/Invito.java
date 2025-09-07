package com.filiera.model.events;

import com.filiera.model.sellers.Venditore;
import lombok.Data;

@Data
public class Invito {

    private int id;

    private AnimatoreFiliera sender;

    private Venditore receiver;

    private String description;

    public Invito(){}

    public Invito(int id, AnimatoreFiliera sender, Venditore receiver, String description) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.description = description;
    }




}
