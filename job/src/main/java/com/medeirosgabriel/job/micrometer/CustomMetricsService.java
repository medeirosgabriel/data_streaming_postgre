package com.medeirosgabriel.job.micrometer;

import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetricsService {
    public static final String NOTIFY_ORDER_UPDATE = "custom.notify.order.update";
    public static final String LISTEN_ORDER_UPDATE = "custom.listen.order.update";

    public CustomMetricsService(CompositeMeterRegistry compositeMeterRegistry) {
    }
}
