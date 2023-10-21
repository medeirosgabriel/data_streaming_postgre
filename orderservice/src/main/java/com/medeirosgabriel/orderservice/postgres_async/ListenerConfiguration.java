package com.medeirosgabriel.orderservice.postgres_async;

import com.zaxxer.hikari.util.DriverDataSource;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.util.Properties;
import java.util.function.Consumer;

import static com.medeirosgabriel.orderservice.postgres_async.NotificationHandler.ORDERS_CHANNEL;

@Configuration
@Slf4j
public class ListenerConfiguration {

    private final JdbcTemplate tpl;

    public ListenerConfiguration(DataSourceProperties props) {
        DriverDataSource ds = new DriverDataSource(
                props.determineUrl(),
                props.determineDriverClassName(),
                new Properties(),
                props.determineUsername(),
                props.determinePassword());
        tpl = new JdbcTemplate(ds);
    }

    @Bean
    CommandLineRunner startListener(NotificationHandler handler) {
        return (args) -> {
            Runnable listener = createNotificationHandler(handler);
            Thread t = new Thread(listener, "order-listener");
            t.start();
        };
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
