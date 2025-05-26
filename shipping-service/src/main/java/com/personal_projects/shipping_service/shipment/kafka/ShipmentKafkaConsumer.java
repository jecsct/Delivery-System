package com.personal_projects.shipping_service.shipment.kafka;

import com.personal_projects.common.Events.OrderEvent;
import com.personal_projects.common.Events.OrderStatusUpdateEvent;
import com.personal_projects.common.Events.ShipmentRequestEvent;
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
 * Kafka consumer configuration for the Shipment service.
 * <p>
 * Sets up the necessary beans to consume {@link ShipmentRequestEvent} messages from a Kafka topic.
 * This configuration uses JSON deserialization to convert message payloads to Java objects.
 * </p>
 */
@Configuration
public class ShipmentKafkaConsumer {

    /**
     * Kafka bootstrap servers, injected from application.properties.
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
     * {@link ShipmentRequestEvent} messages from Kafka.
     *
     * @return a consumer factory for String keys and ShipmentRequestEvent values.
     */
    @Bean
    public ConsumerFactory<String, ShipmentRequestEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfig(),
                new StringDeserializer(),
                new JsonDeserializer<>(ShipmentRequestEvent.class)
        );
    }

    /**
     * Configures the Kafka listener container factory that creates
     * listener containers for processing {@link ShipmentRequestEvent} messages concurrently.
     *
     * @return a Kafka listener container factory.
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShipmentRequestEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ShipmentRequestEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


}
