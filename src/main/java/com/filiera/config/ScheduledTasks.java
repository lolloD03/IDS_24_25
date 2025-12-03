package com.filiera.config;

import com.filiera.services.AnimatoreService;
import com.filiera.services.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {

    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private AnimatoreService animatoreService;

    /**
     * Esegue ogni notte alle 2:00
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredProducts() {
        log.info("Esecuzione task programmata: Eliminazione prodotti scaduti...");


        productService.deleteExpiredProducts_Safe();

    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupExpiredEvents(){
        log.info("Esecuzione task programmata: Eliminazioni eventi passati...");

    animatoreService.deleteExpiredEvents();
    }
}