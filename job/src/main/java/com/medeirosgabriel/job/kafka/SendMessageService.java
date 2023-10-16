package com.medeirosgabriel.job.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medeirosgabriel.job.micrometer.CustomMetricsService;
import com.medeirosgabriel.job.model.Order_;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendMessageService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public SendMessageService() {

    }
    @Timed(
            value = CustomMetricsService.NOTIFY_ORDER_UPDATE,
            description = "Time taken to notify a change from an order update",
            extraTags = {"broker","kafka"}
    )
    public void sendOrderUpdate(Order_ order) {
        try {
            String message = new ObjectMapper().writeValueAsString(order);
            var completableFuture = this.kafkaTemplate.send("order_update", message);
            completableFuture.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("[KAFKA] Sending message to " + message + " `order_update` topic");
                } else {
                    ex.printStackTrace();
                }
            });
        } catch (Exception e) {
            log.error("Some error has occurred: " + e.getMessage());
        }
    }
}

