package com.filiera;

import com.filiera.model.OsmMap.Indirizzo;
import com.filiera.model.administration.Admin;
import com.filiera.model.administration.Curatore;
import com.filiera.model.events.AnimatoreFiliera;
import com.filiera.model.payment.Carrello;
import com.filiera.model.payment.ItemCarrello;
import com.filiera.model.products.Pacchetto;
import com.filiera.model.products.Prodotto;
import com.filiera.model.products.StatoProdotto;
import com.filiera.model.sellers.DistributoreTipicita;
import com.filiera.model.sellers.Produttore;
import com.filiera.model.sellers.Venditore;
import com.filiera.model.users.Acquirente;
import com.filiera.model.users.RuoloUser;
import com.filiera.repository.*;
import com.filiera.services.CarrelloServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@SpringBootApplication
@EnableScheduling
public class FilieraApplication implements CommandLineRunner {
    public static void main(String[] args) {

        SpringApplication.run(FilieraApplication.class, args);


    }


    @Autowired
    InMemoryOrdineRepository ordineRepository;

    @Autowired
    InMemoryProductRepository prodottiRepository;

    @Autowired
    InMemoryPacchettoRepository pacchettoRepository;

    @Autowired
    InMemoryUserRepository userRepository;

    @Autowired
    InMemoryCarrelloRepository carrelloRepository;

    @Autowired
    CarrelloServiceImpl carrelloService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {


        Indirizzo indirizzo1 = new Indirizzo("Milano" , "ViaMilano" , "1");
        Indirizzo indirizzo2 = new Indirizzo("Torino" , "ViaTorino" , "1");



        Venditore venditore1 = Produttore.builder()
                .name("produttore1")
                .address(indirizzo1)
                .approved(true)
                .partitaIva("tuobabbo")
                .email("venditore1@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(RuoloUser.PRODUTTORE)
                .build();

        Venditore venditore2 = Produttore.builder()
                .name("produttore2")
                .address(indirizzo2)
                .approved(true)
                .partitaIva("tuobabbo2")
                .email("venditore2@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(RuoloUser.PRODUTTORE)
                .build();

        Venditore distributoreTipicita = DistributoreTipicita.builder()

                .name("Distributore di tipicità")
                .address(indirizzo1)
                .role(RuoloUser.DISTRIBUTORE)
                .approved(true)
                .email("distributore@gmail.com")
                .password(passwordEncoder.encode("password"))
                .partitaIva("tuobabbo32312")
                .build();

        userRepository.save(venditore1);
        userRepository.save(venditore2);

        Prodotto pomodoro = Prodotto.builder()
                .name("Pomodoro")
                .description("Pomodoro Ciliegino")
                .price(1.99)
                .availableQuantity(5)
                .expirationDate(LocalDate.of(2025, 12, 5)) // Se presente
                .seller(venditore1)
                .build();

        pomodoro.setState(StatoProdotto.APPROVED);

        Prodotto patata = Prodotto.builder()
                .name("Patata buona molto")
                .description("Cultivated")
                .price(2.99)
                .availableQuantity(3)
                .expirationDate(LocalDate.of(2025, 12, 5)) // Se presente
                .seller(venditore1)
                .build();





        Prodotto passataDiPomodoro = Prodotto.builder()
                .name("Passata di pomodoro")
                .description("Trasformated")
                .price(4.99)
                .availableQuantity(1)
                .expirationDate(LocalDate.of(2025, 12, 5)) // Se presente
                .seller(venditore2)
                .build();

        Prodotto cipolla = Prodotto.builder()
                .name("cipolla")
                .description("Trasformated")
                .price(4.99)
                .availableQuantity(2)
                .expirationDate(LocalDate.of(2025, 12, 5)) // Se presente
                .seller(venditore1)
                .build();

        cipolla.setState(StatoProdotto.APPROVED);

        Prodotto anguria = Prodotto.builder()
                .name("Anguria")
                .description("Trasformated")
                .price(10.99)
                .availableQuantity(1)
                .expirationDate(LocalDate.of(2025, 12, 5)) // Se presente
                .seller(venditore2)
                .build();

        anguria.setState(StatoProdotto.APPROVED);


        venditore1.addProduct(cipolla);
        venditore2.addProduct(anguria);
        venditore1.addProduct(pomodoro);
        venditore1.addProduct(patata);
        venditore2.addProduct(passataDiPomodoro);

        prodottiRepository.save(pomodoro);
        prodottiRepository.save(patata);
        prodottiRepository.save(passataDiPomodoro);
        prodottiRepository.save(anguria);
        prodottiRepository.save(cipolla);


        Curatore curatore = Curatore.builder()
                .name("Curatore")
                .role(RuoloUser.CURATORE)
                .email("curatore@gmail.com")
                .approved(true)
                .password(passwordEncoder.encode("password"))
                .build();

        Acquirente buyer1 = Acquirente.builder()
                .name("Acquirente1")
                .approved(true)
                .role(RuoloUser.ACQUIRENTE)
                .email("acquirente1@gmail.com")
                .password(passwordEncoder.encode("password"))
                .build();

        Acquirente buyer2 = Acquirente.builder()
                .name("Acquirente2")
                .role(RuoloUser.ACQUIRENTE)
                .approved(true)
                .email("acquirente2@gmail.com")
                .password(passwordEncoder.encode("password"))
                .build();

        userRepository.save(curatore);

        pomodoro.approveBy(curatore);
        cipolla.approveBy(curatore);
        anguria.approveBy(curatore);


        userRepository.save(buyer1);
        userRepository.save(buyer2);
        userRepository.save(distributoreTipicita);

        Pacchetto pacchettoTipicità = Pacchetto.builder()
                .name("Pacchetto")
                .description("Oggetti")
                .price(20)
                .availableQuantity(3)
                .seller(distributoreTipicita)
                .build();
        pacchettoTipicità.addProduct(cipolla);
        pacchettoTipicità.addProduct(anguria);
        pacchettoTipicità.addProduct(pomodoro);

        pacchettoRepository.save(pacchettoTipicità);

        AnimatoreFiliera animatoreFiliera = AnimatoreFiliera.builder()
                .name("animatore")
                .email("animatore@gmail.com")
                .password(passwordEncoder.encode("password"))
                .approved(true)
                .role(RuoloUser.ANIMATORE)
                .build();
        userRepository.save(animatoreFiliera);

        Admin admin = Admin.builder()
                .name("Admin")
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("password"))
                .role(RuoloUser.ADMIN)
                .approved(true)
                .build();

        userRepository.save(admin);

        System.out.println("Pacchettotipicità ID: " + pacchettoTipicità.getId());
        System.out.println("Venditore1 ID: "+ venditore1.getId());
        System.out.println("Venditore2 ID: "+ venditore2.getId());
        System.out.println("Pomodoro (approved) ID: "+ pomodoro.getId());
        System.out.println("Passata di pomodoro ID: " + passataDiPomodoro.getId());
        System.out.println("Cipolla (approved) ID: " + cipolla.getId());
        System.out.println("Anguria (approved) ID: " + anguria.getId());
        System.out.println("Patata ID: " + patata.getId());

    }


}
