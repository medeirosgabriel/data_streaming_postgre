package com.medeirosgabriel.job.postgres_async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medeirosgabriel.job.SendMessageService;
import com.medeirosgabriel.job.micrometer.CustomMetricsService;
import com.medeirosgabriel.job.model.Order_;
import io.micrometer.core.annotation.Timed;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Profile("POSTGRES")
public class SendMessageServicePostgres implements SendMessageService<Order_> {
    public static final String ORDERS_CHANNEL = "orders";
    private final JdbcTemplate tpl;

    @Autowired
    CustomMetricsService customMetricsService;
    public SendMessageServicePostgres(JdbcTemplate tpl) {
        this.tpl = tpl;
    }
    @Override
    @Transactional
    @Timed(
            value = CustomMetricsService.NOTIFY_ORDER_UPDATE,
            description = "Time taken to notify a change from an order update",
            extraTags = {"broker","postgres"}
    )
    public void sendMessage(Order_ order) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String orderString = objectMapper.writeValueAsString(order);
            log.info("[POSTGRES] sending message to " + orderString + " `order_update` topic");
            tpl.execute("NOTIFY " + ORDERS_CHANNEL + ", '" + orderString + "'");
        } catch (JsonProcessingException e) {
            log.error("fail to write value to json");
        }
    }
}
