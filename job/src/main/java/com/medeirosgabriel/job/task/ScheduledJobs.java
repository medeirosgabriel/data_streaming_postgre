package com.medeirosgabriel.job.task;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ScheduledJobs {
    @Bean
    @ConditionalOnProperty(value = "batch", matchIfMissing = true, havingValue = "true")
    public OrderTask orderTask() {
        return new OrderTask();
    }
}