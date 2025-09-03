package com.filiera.repository;

import com.filiera.model.sellers.Venditore;
import com.filiera.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InMemoryUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.role IN :roles")
    List<Venditore> findByRoleIn(@Param("roles") List<String> roles);
}
