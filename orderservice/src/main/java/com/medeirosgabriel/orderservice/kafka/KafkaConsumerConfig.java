package com.medeirosgabriel.orderservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@Slf4j
@ConditionalOnProperty(value = "spring.profiles.active", havingValue = "KAFKA")
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final String groupId = "foo";

    @Bean
    public ListenMessageService listenMessageService() {
        return new ListenMessageService();
    };

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        if (Objects.equals(activeProfile, "KAFKA")) {
            Map<String, Object> props = new HashMap<>();
            props.put(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    bootstrapAddress);
            props.put(
                    ConsumerConfig.GROUP_ID_CONFIG,
                    groupId);
            props.put(
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                    StringDeserializer.class);
            props.put(
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    StringDeserializer.class);
            return new DefaultKafkaConsumerFactory<>(props);
        } else {
            return null;
        }
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        if (activeProfile.equals("KAFKA")) {
            ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            return factory;
        } else {
            return null;
        }
    }

//    @Bean
//    public ConsumerFactory<String, Order_> orderConsumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(
//                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                bootstrapAddress);
//        props.put(
//                ConsumerConfig.GROUP_ID_CONFIG,
//                groupId);
//        props.put(
//                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class);
//        props.put(
//                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
//                StringDeserializer.class);
//        return new DefaultKafkaConsumerFactory<>(
//                props,
//                new StringDeserializer(),
//                new JsonDeserializer<>(Order_.class));
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Order_>
//    orderKafkaListenerContainerFactory() {
//
//        ConcurrentKafkaListenerContainerFactory<String, Order_> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(orderConsumerFactory());
//        return factory;
//    }
}
