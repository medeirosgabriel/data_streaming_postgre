package com.medeirosgabriel.job.postgres_async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medeirosgabriel.job.micrometer.CustomMetricsService;
import com.medeirosgabriel.job.model.Order_;
import io.micrometer.core.annotation.Timed;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.function.Consumer;

@Slf4j
@Component
public class NotifierService {
    public static final String ORDERS_CHANNEL = "orders";
    private final JdbcTemplate tpl;

    @Autowired
    CustomMetricsService customMetricsService;

    public NotifierService(JdbcTemplate tpl) {
        this.tpl = tpl;
    }

    @Transactional
    @Timed(
            value = CustomMetricsService.NOTIFY_ORDER_UPDATE,
            description = "Time taken to notify a change from an order update",
            extraTags = {"broker","postgres"}
    )
    public void notifyOrderCreated(Order_ order) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String orderString = objectMapper.writeValueAsString(order);
            log.info("[POSTGRES] sending message to " + orderString + " `order_update` topic");
            tpl.execute("NOTIFY " + ORDERS_CHANNEL + ", '" + orderString + "'");
//            customMetricsService.increaseNotifyCounter();
        } catch (JsonProcessingException e) {
            log.error("fail to write value to json");
        }
    }

}