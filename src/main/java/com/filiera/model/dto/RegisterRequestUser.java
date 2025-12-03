package com.filiera.model.dto;

import com.filiera.model.users.RuoloUser;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequestUser {
    private String email;
    private String password;
    private String name;
    private RuoloUser ruoloUser;
}
