package com.filiera.controller;

import com.filiera.model.users.User;
import com.filiera.services.UserService;

import java.util.UUID;

public class UserController {

    private final UserService service;
    public UserController(UserService service) { this.service = service; }


    public User register(User user) {
        return service.register(user);
    }

    public User getUser(UUID id) {
        return service.findById(id);
    }

    public void deleteById(UUID id) {service.deleteById(id);}

}
