package com.filiera.repository;

import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;

import java.util.*;

public class InMemoryProductRepository implements CrudRepository<Prodotto, UUID> {
    private final Map<UUID, Prodotto> store = new HashMap<>();
    @Override public Prodotto save(Prodotto p) { store.put(p.getId(), p); return p; }
    @Override public Optional<Prodotto> findById(UUID id) { return Optional.ofNullable(store.get(id)); }
    @Override public List<Prodotto> findAll() { return new ArrayList<>(store.values()); }
    @Override public void deleteById(UUID id) { store.remove(id); }

    public List<Prodotto> findByState(StatoProdotto stato) {
        List<Prodotto> result = new ArrayList<>();
        for (Prodotto prodotto : store.values()) {
            if (prodotto.getState() == stato) {
                result.add(prodotto);
            }
        }
        return result;
    }

    public List<Prodotto> getApprovedProducts() {
        return findByState(StatoProdotto.APPROVATO);
    }

}
