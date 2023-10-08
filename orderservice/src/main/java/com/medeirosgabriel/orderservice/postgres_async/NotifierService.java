package com.medeirosgabriel.orderservice.postgres_async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medeirosgabriel.orderservice.model.Order_;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.util.function.Consumer;

@Slf4j

public class NotifierService {
    public static final String ORDERS_CHANNEL = "orders";
    private final JdbcTemplate tpl;

    public NotifierService(JdbcTemplate tpl) {
        this.tpl = tpl;
    }

    @Transactional
    public void notifyOrderCreated(Order_ order) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String orderString = objectMapper.writeValueAsString(order);
            tpl.execute("NOTIFY " + ORDERS_CHANNEL + ", '" + orderString + "'");
        } catch (JsonProcessingException e) {
            log.error("fail to write value to json");
        }
    }

    public Runnable createNotificationHandler(Consumer<PGNotification> consumer) {
        return () -> {
            tpl.execute((Connection c) -> {
                c.createStatement().execute("LISTEN " + ORDERS_CHANNEL);
                PGConnection pgconn = c.unwrap(PGConnection.class);
                while(!Thread.currentThread().isInterrupted()) {
                    PGNotification[] nts = pgconn.getNotifications(10000);
                    if ( nts == null || nts.length == 0 ) {
                        continue;
                    }
                    for( PGNotification nt : nts) {
                        consumer.accept(nt);
                    }
                }
                return 0;
            });
        };
    }

}