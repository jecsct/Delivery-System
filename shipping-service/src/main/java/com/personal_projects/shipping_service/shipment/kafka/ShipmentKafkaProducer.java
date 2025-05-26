package com.personal_projects.shipping_service.shipment.kafka;


import com.personal_projects.common.Events.OrderStatusUpdateEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


/**
 * Kafka producer configuration for the Shipment service.
 * <p>
 * This class sets up the necessary beans to send {@link OrderStatusUpdateEvent} messages to Kafka.
 * </p>
 */
@Configuration
public class ShipmentKafkaProducer {

    /**
     * Kafka broker address, injected from application properties (e.g., application.yml or application.properties).
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Builds a map of configuration properties common to all Kafka producers.
     *
     * @return a map of Kafka producer configuration properties.
     */
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /**
     * Creates a {@link ProducerFactory} for producing {@link OrderStatusUpdateEvent} messages.
     *
     * @return a producer factory configured for {@code OrderStatusUpdateEvent}.
     */
    @Bean
    public ProducerFactory<String, OrderStatusUpdateEvent> orderStatusUpdateProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Creates a {@link KafkaTemplate} for sending {@link OrderStatusUpdateEvent} messages.
     *
     * @return a Kafka template configured for {@code OrderStatusUpdateEvent}.
     */
    @Bean
    public KafkaTemplate<String, OrderStatusUpdateEvent> orderStatusUpdateKafkaTemplate() {
        return new KafkaTemplate<>(orderStatusUpdateProducerFactory());
    }

}
