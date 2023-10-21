package com.medeirosgabriel.job.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medeirosgabriel.job.SendMessageService;
import com.medeirosgabriel.job.micrometer.CustomMetricsService;
import com.medeirosgabriel.job.model.Order_;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("KAFKA")
public class SendMessageServiceKafka implements SendMessageService<Order_> {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public SendMessageServiceKafka(@Nullable KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @Timed(
            value = CustomMetricsService.NOTIFY_ORDER_UPDATE,
            description = "Time taken to notify a change from an order update",
            extraTags = {"broker","kafka"}
    )
    @Override
    public void sendMessage(Order_ order) {
        try {
            String message = new ObjectMapper().writeValueAsString(order);
            assert this.kafkaTemplate != null;
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
    //    public void sendOrderUpdate(Order_ order) {
//        try {
//            String message = new ObjectMapper().writeValueAsString(order);
//            var completableFuture = this.kafkaTemplate.send("order_update", message);
//            completableFuture.whenComplete((result, ex) -> {
//                if (ex == null) {
//                    log.info("[KAFKA] Sending message to " + message + " `order_update` topic");
//                } else {
//                    ex.printStackTrace();
//                }
//            });
//        } catch (Exception e) {
//            log.error("Some error has occurred: " + e.getMessage());
//        }
//    }
}

