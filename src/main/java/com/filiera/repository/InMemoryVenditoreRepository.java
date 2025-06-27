package com.filiera.repository;

import com.filiera.model.sellers.Venditore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InMemoryVenditoreRepository extends JpaRepository<Venditore  , UUID> { }
