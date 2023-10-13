package com.medeirosgabriel.orderservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medeirosgabriel.orderservice.model.Order_;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SendMessageService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public SendMessageService(final KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderUpdate(Order_ order) {
        try {
            this.kafkaTemplate.send("order_update", new ObjectMapper().writeValueAsString(order));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

