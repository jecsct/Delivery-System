package com.personal_projects.shipping_service.shipment.kafka;

import com.personal_projects.common.Events.PaymentEvent;
import com.personal_projects.common.Events.ShipmentEvent;
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
 *
 * <p>This configuration sets up Kafka consumers specifically for the {@link PaymentEvent} type.
 * It defines the necessary beans and deserialization settings used for consuming messages from Kafka.</p>
 */
@Configuration
public class ShipmentKafkaConsumer {

    /**
     * Kafka bootstrap servers address, injected from application properties.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Builds a map of configuration properties common to all Kafka consumers.
     *
     * @return a map of Kafka consumer configuration properties
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
     * Creates a {@link ConsumerFactory} for consuming {@link PaymentEvent} messages.
     *
     * @return a consumer factory configured for {@link PaymentEvent}
     */
    @Bean
    public ConsumerFactory<String, PaymentEvent> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfig(),
                new StringDeserializer(),
                new JsonDeserializer<>(PaymentEvent.class)
        );
    }

    /**
     * Creates a {@link ConcurrentKafkaListenerContainerFactory} for consuming {@link PaymentEvent} messages.
     *
     * @return a Kafka listener container factory for {@link PaymentEvent}
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> shipmentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
