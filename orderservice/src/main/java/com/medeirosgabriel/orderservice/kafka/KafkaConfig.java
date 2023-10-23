package com.medeirosgabriel.orderservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerConfigUtils;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@Slf4j
@ConditionalOnProperty(value = "spring.profiles.active", havingValue = "KAFKA")
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    @ConditionalOnProperty(value = "spring.profiles.active", havingValue = "KAFKA")
    public KafkaAdmin kafkaAdmin() {
        if (activeProfile.equals("KAFKA")) {
            Map<String, Object> configs = new HashMap<>();
            configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
            return new KafkaAdmin(configs);
        } else {
            return null;
        }
    }

    @Bean
    @ConditionalOnProperty(value = "spring.profiles.active", havingValue = "KAFKA")
    public NewTopic topic1() {
        return new NewTopic("order_update", 1, (short) 1);
    }
}
