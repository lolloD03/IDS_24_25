package com.filiera.model.sellers;


import com.filiera.model.products.Prodotto;
import com.filiera.model.users.RuoloUser;

import java.util.UUID;

public  class Trasformatore extends Venditore {

    private String process;
    private String certification;

    public Trasformatore() {
        super();
    }



    public Trasformatore(String password, String email, String name, RuoloUser ruoloUser, int partitaIva ) {
        super( password, email, name, ruoloUser ,partitaIva);
        this.process = process;
        this.certification = certification;
    }

    public String getProductionProcess() {
        return process;
    }

    public void setProductionProcess(String productionProcess) {
        this.process = productionProcess;
    }


}
