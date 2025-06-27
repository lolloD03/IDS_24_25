package com.filiera.services;


import com.filiera.model.users.User;

import java.util.UUID;

public class    UserServiceImpl implements UserService {
    private final CrudRepository<User, UUID> repo;
    public UserServiceImpl(InMemoryUserRepository repo) { this.repo = repo; }
    @Override
    public User register(User user) {

        if (repo.findAll().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new IllegalArgumentException("Email già registrata!");
        }
        return repo.save(user);
    }

    @Override
    public User findById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(("Utente non trovato con id: " + id)));
    }

    public void deleteById(UUID id){ repo.deleteById(id); }

}
