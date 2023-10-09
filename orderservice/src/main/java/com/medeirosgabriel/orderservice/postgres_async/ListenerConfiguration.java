package com.medeirosgabriel.orderservice.postgres_async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ListenerConfiguration {

    @Bean
    CommandLineRunner startListener(NotifierService notifier, NotificationHandler handler) {
        return (args) -> {
            Runnable listener = notifier.createNotificationHandler(handler);
            Thread t = new Thread(listener, "order-listener");
            t.start();
        };
    }
}
