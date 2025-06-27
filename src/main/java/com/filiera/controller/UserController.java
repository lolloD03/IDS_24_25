package com.filiera.controller;
import com.filiera.model.sellers.Produttore;
import com.filiera.model.users.User;
import com.filiera.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) { this.service = service; }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }

    @PostMapping("/register/produttore")
    public User registerProduttore(@RequestBody Produttore producer) {
        if (producer == null) {
            throw new IllegalArgumentException("Produttore cannot be null");
        }
        return service.registerProducer(producer);
    }

    @GetMapping("/all")
    public Iterable<User> getAllUsers() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable UUID id) {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable UUID id) {service.deleteById(id);}

}

