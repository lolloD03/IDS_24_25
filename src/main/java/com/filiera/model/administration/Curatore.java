package com.filiera.model.administration;

import com.filiera.model.users.RuoloUser;
import com.filiera.model.users.User;

public class Curatore extends User {

    public Curatore() {super();}

    public Curatore(String password, String email, String name, RuoloUser ruoloUser) {
        super( password, email, name, ruoloUser);
    }

}
