package com.filiera.services;

import com.filiera.model.users.User;

import com.filiera.model.sellers.Produttore;
import com.filiera.model.users.User;

import java.util.UUID;

public interface UserService {
    User register(User user);
    User findById(UUID id);
    void deleteById(UUID id);

    Iterable<User> findAll();

    User registerProducer(Produttore producer);
}