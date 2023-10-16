package com.medeirosgabriel.orderservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medeirosgabriel.orderservice.micrometer.CustomMetricsService;
import com.medeirosgabriel.orderservice.model.Order_;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ListenMessageService {

    @KafkaListener(topics = "order_update", groupId = "foo")
    @Timed(
            value = CustomMetricsService.LISTEN_ORDER_UPDATE,
            description = "Time taken to listen a change from an order update",
            extraTags = {"broker","kafka"}
    )
    public void handleOrder_(String message) {
        log.info("Received message in topic order_update: " + message);
    }

}
