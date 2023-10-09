package com.medeirosgabriel.orderservice.postgres_async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medeirosgabriel.orderservice.micrometer.CustomMetricsService;
import com.medeirosgabriel.orderservice.model.Order_;
import com.medeirosgabriel.orderservice.repository.OrderRepository;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
@Timed
public class NotificationHandler implements Consumer<PGNotification> {

    private final OrderRepository orderRepository;

    @Autowired
    CustomMetricsService customMetricsService;

    @Override
    @Timed(
            value = CustomMetricsService.LISTEN_ORDER_UPDATE,
            description = "Time taken to listen a change from an order update",
            extraTags = {"broker","postgres"}
    )
    public void accept(PGNotification t) {
        log.info("Notification received: pid={}, name={}, param={}",t.getPID(),t.getName(),t.getParameter());
        if (t.getName().equals(NotifierService.ORDERS_CHANNEL)) {
            try {
                Order_ order = new ObjectMapper().readValue(t.getParameter(), Order_.class);
                Optional<Order_> optionalOrder = orderRepository.findById(order.getId());
                optionalOrder.ifPresent(order_ -> log.info("order details: {}", order_));
            } catch (JsonProcessingException e) {
                log.error("fail to read value from json");
            }
        }
    }

}