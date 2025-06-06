package com.personal_projects.shipping_service.shipment.kafka;


import com.personal_projects.common.Events.PaymentEvent;
import com.personal_projects.common.Events.ShipmentEvent;
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
 *
 * <p>This configuration sets up the necessary beans to produce messages of type {@link ShipmentEvent}
 * to Kafka. It defines the producer properties such as bootstrap servers and serializers,
 * and exposes a {@link KafkaTemplate} bean to facilitate sending messages.</p>
 */
@Configuration
public class ShipmentKafkaProducer {

    /**
     * Kafka broker address, injected from application properties (e.g., application.yml or application.properties).
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Builds a map of configuration properties used by Kafka producers.
     *
     * @return a map containing Kafka producer configuration properties including
     * bootstrap servers and serializers for key and value.
     */
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /**
     * Creates a {@link ProducerFactory} configured for producing {@link ShipmentEvent} messages.
     *
     * @return a producer factory for {@code ShipmentEvent}
     */
    @Bean
    public ProducerFactory<String, ShipmentEvent> shipmentProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * Creates a {@link KafkaTemplate} bean to send {@link ShipmentEvent} messages to Kafka.
     *
     * @return a Kafka template for sending {@code ShipmentEvent} messages
     */
    @Bean
    public KafkaTemplate<String, ShipmentEvent> shipmentKafkaTemplate() {
        return new KafkaTemplate<>(shipmentProducerFactory());
    }
}

