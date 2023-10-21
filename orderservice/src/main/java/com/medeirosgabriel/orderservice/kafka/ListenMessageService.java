package com.medeirosgabriel.orderservice.kafka;

import com.medeirosgabriel.orderservice.micrometer.CustomMetricsService;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class ListenMessageService {

    @KafkaListener(topics = "order_update", groupId = "foo", autoStartup = "#{'${spring.profiles.active}' == 'KAFKA'}")
    @Timed(
            value = CustomMetricsService.LISTEN_ORDER_UPDATE,
            description = "Time taken to listen a change from an order update",
            extraTags = {"broker","kafka"}
    )
    public void handleOrder_(String message) {
        log.info("Received message in topic order_update: " + message);
    }

}
