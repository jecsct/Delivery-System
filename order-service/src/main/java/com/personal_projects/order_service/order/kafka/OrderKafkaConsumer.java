package com.personal_projects.order_service.order.kafka;

import com.personal_projects.common.Events.OrderEvent;
import com.personal_projects.common.Events.OrderStatusUpdateEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka consumer configuration for the Order Service.
 * <p>
 * This class defines how the Order Service consumes {@link OrderStatusUpdateEvent}
 * messages from Kafka. It sets up deserializers, consumer properties, and
 * the listener container factory.
 * </p>
 */
@Configuration
public class OrderKafkaConsumer {

    /**
     * Kafka bootstrap servers, injected from application properties.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Creates a configuration map for Kafka consumer.
     *
     * @return a map containing the Kafka consumer configuration properties.
     */
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*"); // Allows deserialization of any package
        return props;
    }

    /**
     * Configures the {@link ConsumerFactory} used to deserialize
     * {@link OrderStatusUpdateEvent} messages from Kafka.
     *
     * @return a consumer factory for String keys and OrderStatusUpdateEvent values.
     */
    @Bean
    public ConsumerFactory<String, OrderStatusUpdateEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfig(),
                new StringDeserializer(),
                new JsonDeserializer<>(OrderStatusUpdateEvent.class)
        );
    }

    /**
     * Configures the Kafka listener container factory that creates
     * listener containers for processing Kafka messages concurrently.
     *
     * @return a Kafka listener container factory.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderStatusUpdateEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderStatusUpdateEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
