package com.filiera.services;

import com.filiera.model.OsmMap.Indirizzo;
import com.filiera.model.administration.Curatore;
import com.filiera.model.sellers.Produttore;
import com.filiera.model.sellers.Venditore;
import com.filiera.model.users.Acquirente;
import com.filiera.model.users.RuoloUser;
import com.filiera.repository.InMemoryUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final InMemoryUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(InMemoryUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
    public Venditore registerVenditore(String username, String rawPassword, String nomeAzienda) {
        Venditore venditore = new Venditore();
        venditore.setUsername(username);
        venditore.setPassword(passwordEncoder.encode(rawPassword));
        venditore.setNomeAzienda(nomeAzienda);
        return userRepository.save(venditore);
    }
*/
    public Curatore registerCuratore(String name,String email ,String rawPassword) {
        Curatore curatore = new Curatore();
        curatore.setName(name);
        curatore.setEmail(email);
        curatore.setPassword(passwordEncoder.encode(rawPassword));
        curatore.setRole(RuoloUser.CURATORE);
        return userRepository.save(curatore);
    }

    public Venditore registerProduttore(String name, String email, String rawPassword, String citta , String via, String numeroCivico, String partitaIva, String processo) {
        Indirizzo indirizzo = new Indirizzo(citta,via,numeroCivico );

        Produttore produttore = new Produttore();
        produttore.setName(name);
        produttore.setEmail(email);
        produttore.setPassword(passwordEncoder.encode(rawPassword));
        produttore.setAddress(indirizzo);
        produttore.setPartitaIva(partitaIva);
        produttore.setProcess(processo);
        return userRepository.save(produttore);
    }
    public Venditore registerTrasformatore(String name, String email, String rawPassword, String citta , String via, String numeroCivico, String partitaIva, String processo) {
        Indirizzo indirizzo = new Indirizzo(citta,via,numeroCivico );

        Produttore produttore = new Produttore();
        produttore.setName(name);
        produttore.setEmail(email);
        produttore.setPassword(passwordEncoder.encode(rawPassword));
        produttore.setAddress(indirizzo);
        produttore.setPartitaIva(partitaIva);
        produttore.setProcess(processo);
        return userRepository.save(produttore);
    }


    public Acquirente registerAcquirente(String name,String email ,String rawPassword, String citta,String via,String numeroCivico) {
        Indirizzo indirizzo = new Indirizzo(citta,via,numeroCivico);
        Acquirente acquirente = new Acquirente();
        acquirente.setName(name);
        acquirente.setEmail(email);
        acquirente.setPassword(passwordEncoder.encode(rawPassword));
        acquirente.setIndirizzo(indirizzo);
        return userRepository.save(acquirente);
    }
}
